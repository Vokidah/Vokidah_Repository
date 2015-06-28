package org.khadikov.projectname.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.google.gson.*;
import org.khadikov.projectname.annotations.*;
import org.khadikov.projectname.scanner.ClassPathScanner;

public class ListenServlet extends HttpServlet {
    String packageName;
    Map<String, Class> handlers;

    public void init() {
        packageName = "org.khadikov.projectname";
        handlers = new ClassPathScanner(packageName).getRestHandlers();
    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, NoClassDefFoundError {
        String method = req.getMethod();
        String URI = req.getRequestURI();
        Object toPrint = null;
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        if (isContains(URI)) {
            try {
                if (!handlers.isEmpty()) {
                    for (Map.Entry<String, Class> entry : handlers.entrySet()) {
                        String key = entry.getKey();
                        Class value = entry.getValue();
                        if (URI.contains(key)) {
                            if (method.equals("GET")) {
                                resp.setContentType("\"application/json\"");
                                if (URI.equals(key) || URI.equals(key + "/")) {
                                    toPrint = this.get_Object(null, value, "", method);
                                    if (toPrint != null) {
                                        out.println(toPrint.toString());
                                    } else
                                        resp.sendError(404, "NOT FOUND");

                                } else {
                                    ArrayList<Object> ob = new ArrayList<Object>();
                                    ob.add(URI.substring(key.length() + 1, URI.length()).toString());
                                    toPrint = this.get_Object(ob, value, "/:id", method);
                                    if (toPrint != null) {
                                        out.println(toPrint.toString());
                                    } else
                                        resp.sendError(404, "NOT FOUND");
                                }
                            } else if (method.equals("POST")) {
                                resp.setContentType("\"application/json\"");
                                BufferedReader reader = req.getReader();
                                Class a = handlers.get("class_user");
                                ArrayList<Object> ob = new ArrayList<Object>();
                                Object obj = gson.fromJson(reader, a);
                                ob.add(obj);
                                out.println(ob.get(0).toString());
                                if (URI.equals(key) || URI.equals(key + "/")) {
                                    toPrint = this.get_Object(ob, value, "", method);
                                    out.println(toPrint);
                                    if (toPrint.equals(false)) {
                                        resp.sendError(500, "can`t add");
                                    } else {
                                        resp.setStatus(201);
                                    }
                                }
                                else{
                                    resp.sendError(404,"NOT FOUND");
                                }
                            } else if (method.equals("PUT")) {
                                resp.setContentType("\"application/json\"");
                                BufferedReader reader = req.getReader();
                                Class a = handlers.get("class_user");
                                ArrayList<Object> ob = new ArrayList<Object>();
                                Object obj = gson.fromJson(reader, a);
                                ob.add(obj);
                                out.println(ob.get(0).toString());
                                if (!(URI.equals(key) || URI.equals(key + "/"))){
                                    toPrint = this.get_Object(ob, value, "/:id", method);
                                    out.println(toPrint);
                                    if (toPrint.equals(false)) {
                                        resp.sendError(500, "can`t add");
                                    } else {
                                        resp.setStatus(201);
                                    }
                                }
                                else{
                                    resp.sendError(404,"NOT FOUND");
                                }

                            } else if (method.equals("DELETE")) {
                                resp.setContentType("\"application/json\"");
                                if (!(URI.equals(key) || URI.equals(key + "/")))
                                {
                                    ArrayList<Object> ob = new ArrayList<Object>();
                                    ob.add(URI.substring(key.length() + 1, URI.length()).toString());
                                    toPrint = this.get_Object(ob, value, "/:id", method);
                                    if (toPrint != null) {
                                        out.println(toPrint.toString());
                                    } else
                                        resp.sendError(404, "NOT FOUND");
                                }
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
        } else {
            resp.sendError(404, "NOT FOUND");
        }

    }

    public boolean isContains(String URI) {
        boolean check = false;
        for (Map.Entry<String, Class> entry : handlers.entrySet()) {
            String key = entry.getKey();
            if (URI.contains(key + "/") || (URI.contains(key) && URI.substring(URI.length() - key.length()).equals(key)))
                check = true;
        }
        return check;
    }

    public Method get_method(Method[] m, String value, String req_type) {
        for (int i = 0; i < m.length; i++) {
            Annotation[] a = m[i].getDeclaredAnnotations();
            for (Annotation annotation : a) {
                if (req_type.equals("GET")) {
                    if (annotation instanceof Get) {
                        Get myAnnotation = (Get) annotation;
                        if (value.equals(myAnnotation.value()))
                            return m[i];
                    }
                } else if (req_type.equals("POST")) {
                    if (annotation instanceof Post) {

                        Post myAnnotation = (Post) annotation;
                        if (value.equals(""))
                            return m[i];
                    }
                } else if (req_type.equals("PUT")) {
                    if (annotation instanceof Put) {
                        Put myAnnotation = (Put) annotation;
                        if (value.equals(myAnnotation.value()))
                            return m[i];
                    }
                } else if (req_type.equals("DELETE")) {
                    if (annotation instanceof Delete) {
                        Delete myAnnotation = (Delete) annotation;
                        if (value.equals(myAnnotation.value()))
                            return m[i];
                    }
                }

            }

        }

        return null;
    }

    public Object get_Object(ArrayList<Object> id, Class value, String path, String req_type) throws InvocationTargetException,
            IllegalAccessException, InstantiationException {
        Method m = get_method(value.getDeclaredMethods(), path, req_type);
        Object obj = value.newInstance();
        try {
            Object[] args = new Object[id.size()];
            for (int i = 0; i < id.size(); i++)
                args[i] = id.get(i);

            if (!args.equals(null)) {
                return m.invoke(obj, args);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return m.invoke(obj, null);

    }
}
