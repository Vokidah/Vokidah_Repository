package org.khadikov.projectname.servlet;

import com.google.gson.Gson;
import org.khadikov.projectname.annotations.*;
import org.khadikov.projectname.http.RequestType;
import org.khadikov.projectname.http.Responses;
import org.khadikov.projectname.parser.StringParser;
import org.khadikov.projectname.scanner.ClassPathScanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class ListenServlet extends HttpServlet {
    public static final String BASE_PATH_PARAM = "basePath";
    private static final String PACKAGE_NAME = "rootPackageToScan";
    public static final String JSP_SUFFIX = ".jsp";

    private Map<String, Class> handlers;
    private Map<String, Object> instances;
    private String basePath;

    public void init() {
        this.basePath = this.getServletConfig().getInitParameter(BASE_PATH_PARAM);
        String packageName = this.getServletConfig().getInitParameter(PACKAGE_NAME);
        this.handlers = new ClassPathScanner(packageName).getRestHandlers();
        this.instances = new ClassPathScanner(packageName).getRestInstances();

    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NoClassDefFoundError {
        String reqMethod = req.getMethod();
        String URI = req.getRequestURI();
        Object result;
        Method method;
        Gson gson = new Gson();
        BufferedReader reader = req.getReader();
        if (URI.startsWith(this.basePath)) {
            URI = URI.substring(this.basePath.length(), URI.length());
        }
        if (contains(URI)) {
            try {

                if (!handlers.isEmpty()) {

                    for (Map.Entry<String, Class> entry : handlers.entrySet()) {
                        String path = entry.getKey();
                        Class handlerClass = entry.getValue();
                        Object handler = instances.get(path);
                        String Get_type;
                        StringParser parser = new StringParser(URI, path);
                        if (URI.contains(path)) {
                            if (reqMethod.equals(RequestType.GET.value())) {
                                resp.setContentType("application/json;charset=UTF-8");
                                if (parser.Check_our_String(RequestType.GET)) {
                                    if (parser.getID().equals("")) {
                                        Get_type = "";
                                        method = getMethod(handlerClass, path, RequestType.GET, Get_type);
                                        result = method.invoke(handler, null);
                                    } else {
                                        Get_type = "/:id";
                                        method = getMethod(handlerClass, path, RequestType.GET, Get_type);
                                        result = method.invoke(handler, parser.getID());

                                    }
                                    if (result != null) {
                                        resp.getOutputStream().write(gson.toJson(result).getBytes());
                                        resp.getOutputStream().close();
                                    } else {
                                        resp.sendError(Responses.NOT_FOUND.getCode(),Responses.NOT_FOUND.getMessage());
                                    }
                                } else {
                                    resp.sendError(Responses.NOT_FOUND.getCode(),Responses.NOT_FOUND.getMessage());
                                }

                            } else if (reqMethod.equals(RequestType.POST.value())) {
                                Get_type = "";
                                if (parser.Check_our_String(RequestType.POST)) {
                                    method = getMethod(handlerClass, path, RequestType.POST, Get_type);
                                    result = method.invoke(handler, gson.fromJson(reader, find_class(method)));
                                    if (result.equals(true)) {
                                        resp.setStatus(201);
                                    } else {
                                        resp.sendError(500, "Can`t add");
                                    }
                                } else {
                                    resp.sendError(Responses.NOT_FOUND.getCode(),Responses.NOT_FOUND.getMessage());
                                }

                            } else if (reqMethod.equals(RequestType.PUT.value())) {
                                Get_type = "/:id";
                                if (parser.Check_our_String(RequestType.PUT)) {
                                    method = getMethod(handlerClass, path, RequestType.PUT, Get_type);
                                    result = method.invoke(handler, gson.fromJson(reader, find_class(method)));
                                    if (result.equals(true)) {
                                        resp.setStatus(201);
                                    } else {
                                        resp.sendError(500, "Can`t add");
                                    }
                                }


                            } else if (reqMethod.equals(RequestType.DELETE.value())) {
                                Get_type = "/:id";
                                resp.setContentType("text/html");
                                if (parser.Check_our_String(RequestType.DELETE)) {
                                    method = getMethod(handlerClass, path, RequestType.DELETE, Get_type);
                                    result = method.invoke(handler, parser.getID());
                                    if (result.equals(true)) {
                                        resp.setStatus(201);
                                    } else {
                                        resp.sendError(Responses.NOT_FOUND.getCode(),Responses.NOT_FOUND.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (!URI.endsWith(JSP_SUFFIX)) {
            resp.sendError(Responses.NOT_FOUND.getCode(), Responses.NOT_FOUND.getMessage());
        }

    }

    public boolean contains(String URI) {
        boolean check = false;

        for (Map.Entry<String, Class> entry : handlers.entrySet()) {
            String key = entry.getKey();

            if (URI.contains(key + "/")
                    || (URI.contains(key) && URI.substring(URI.length() - key.length()).equals(key)))
                check = true;
        }

        return check;
    }

    public Method getMethod(Class handlerClass,
                            String path,
                            RequestType requestType, String type) {
        Method[] methods = handlerClass.getDeclaredMethods();

        for (int atIndex = 0; atIndex < methods.length; atIndex++) {
            Method currentMethod = methods[atIndex];
            Annotation[] methodAnnotations = currentMethod.getDeclaredAnnotations();

            for (Annotation annotation : methodAnnotations) {
                if (requestType.equals(RequestType.GET)) {
                    if (annotation instanceof Get) {
                        Get myAnnotation = (Get) annotation;
                        if (type.equals(myAnnotation.value()))
                            return currentMethod;
                    }
                } else if (requestType.equals(RequestType.POST)) {
                    if (annotation instanceof Post) {
                        Post myAnnotation = (Post) annotation;
                        if (type.equals(""))
                            return currentMethod;
                    }
                } else if (requestType.equals(RequestType.PUT)) {
                    if (annotation instanceof Put) {
                        Put myAnnotation = (Put) annotation;
                        if (type.equals(myAnnotation.value()))
                            return currentMethod;
                    }
                } else if (requestType.equals(RequestType.DELETE)) {
                    if (annotation instanceof Delete) {
                        Delete myAnnotation = (Delete) annotation;
                        if (type.equals(myAnnotation.value()))
                            return currentMethod;
                    }
                }

            }

        }

        return null;
    }

    public Class find_class(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class[] parameterTypes = method.getParameterTypes();

        int i = 0;
        for (Annotation[] annotations : parameterAnnotations) {
            Class parameterType = parameterTypes[i++];

            for (Annotation annotation : annotations) {
                if (annotation instanceof Body) {
                    Body myAnnotation = (Body) annotation;
                    return parameterType;
                }
            }
        }
        return null;
    }
}
