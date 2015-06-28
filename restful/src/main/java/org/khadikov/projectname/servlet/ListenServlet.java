package org.khadikov.projectname.servlet;

import com.google.gson.Gson;
import org.khadikov.projectname.annotations.Delete;
import org.khadikov.projectname.annotations.Get;
import org.khadikov.projectname.annotations.Post;
import org.khadikov.projectname.annotations.Put;
import org.khadikov.projectname.http.RequestType;
import org.khadikov.projectname.http.Responses;
import org.khadikov.projectname.scanner.ClassPathScanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private String basePath;

    public void init() {
        this.basePath = this.getServletConfig().getInitParameter(BASE_PATH_PARAM);
        String packageName = this.getServletConfig().getInitParameter(PACKAGE_NAME);

        this.handlers = new ClassPathScanner(packageName).getRestHandlers();
    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NoClassDefFoundError {
        String reqMethod = req.getMethod();
        String URI = req.getRequestURI();

        if (URI.startsWith(this.basePath)) {
            URI = URI.substring(this.basePath.length(), URI.length());
        }

        if (contains(URI)) {
            try {

                if (!handlers.isEmpty()) {
                    for (Map.Entry<String, Class> entry : handlers.entrySet()) {
                        String path = entry.getKey();
                        Class handlerClass = entry.getValue();

                        if (URI.contains(path)) {

                            if (reqMethod.equals(RequestType.GET.value())) {
                                resp.setContentType("application/json;charset=UTF-8");

                                Method method = getMethod(handlerClass,
                                        path, RequestType.GET);

                                Object handler = handlerClass.newInstance();

                                Object result = method.invoke(handler, null);

                                Gson gson = new Gson();
                                String responseString = gson.toJson(result);

                                resp.getOutputStream().write(responseString.getBytes());
                                resp.getOutputStream().close();

                            } else if (reqMethod.equals(RequestType.POST.value())) {

                            } else if (reqMethod.equals(RequestType.PUT.value())) {

                            } else if (reqMethod.equals(RequestType.DELETE.value())) {

                            }
                        }
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
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
                            RequestType requestType) {
        String defaultGetRoute = "/" + handlerClass.getSimpleName().toLowerCase();

        Method[] methods = handlerClass.getDeclaredMethods();

        for (int atIndex = 0; atIndex < methods.length; atIndex++) {
            Method currentMethod = methods[atIndex];
            Annotation[] methodAnnotations = currentMethod.getDeclaredAnnotations();

            for (Annotation annotation : methodAnnotations) {
                if (requestType.equals(RequestType.GET)) {
                    if (annotation instanceof Get) {
                        Get myAnnotation = (Get) annotation;

                        boolean isEmpty = ((Get) annotation).value().equals("");
                        if(path.equals(defaultGetRoute) && isEmpty){
                            return currentMethod;
                        }

                        if (path.equals(myAnnotation.value())) {
                            return currentMethod;
                        }
                    }
                } else if (requestType.equals(RequestType.POST)) {
                    if (annotation instanceof Post) {

                        Post myAnnotation = (Post) annotation;
                        if (path.equals(""))
                            return currentMethod;
                    }
                } else if (requestType.equals(RequestType.PUT)) {
                    if (annotation instanceof Put) {
                        Put myAnnotation = (Put) annotation;

                        if (path.equals(myAnnotation.value()))
                            return currentMethod;
                    }
                } else if (requestType.equals(RequestType.DELETE)) {
                    if (annotation instanceof Delete) {
                        Delete myAnnotation = (Delete) annotation;
                        if (path.equals(myAnnotation.value()))
                            return currentMethod;
                    }
                }

            }

        }

        return null;
    }
}
