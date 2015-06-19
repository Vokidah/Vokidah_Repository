package org.khadikov.projectname.servlet;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.khadikov.projectname.annotations.Get;
import org.khadikov.projectname.scanner.ClassPathScanner;

public class ListenServlet extends HttpServlet {
    String packageName;
    Map<String, Class> handlers;
    Class listen;

    public void init() {
        packageName = "org.khadikov.projectname";
        handlers = new ClassPathScanner(packageName).getRestHandlers();
    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String URI = req.getRequestURI();
        PrintWriter out = resp.getWriter();
        if (isContains(URI)) {
            try {
                if (method.equals("GET")) {
                    for (Map.Entry<String, Class> entry : handlers.entrySet()) {
                        String key = entry.getKey();
                        Class value = entry.getValue();


                        if (URI.equals(key) || URI.equals(key + "/")) {
                            Method m = get_method(value.getDeclaredMethods(), "");
                            Object obj =  value.newInstance();
                            Object toPrint=m.invoke(obj, null);
                            if(toPrint!=null)
                                out.print(toPrint);
                            else
                                resp.sendError(404,"NOT FOUND");
                        }
                        else{
                            Method m = get_method(value.getDeclaredMethods(), "/:id");
                            Object obj = value.newInstance();
                            String id=URI.substring(key.length()+1,URI.length());
                            Object toPrint=m.invoke(obj, id);
                            if(toPrint!=null)
                                out.print(toPrint);
                            else
                                resp.sendError(404,"NOT FOUND");
                        }

                    }
                } else if (method.equals("POST")) {

                } else if (method.equals("PUT")) {

                } else if (method.equals("DELETE")) {

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

    public Method get_method(Method[] m, String value) {
        for (int i = 0; i < m.length; i++) {
            Annotation[] a = m[i].getDeclaredAnnotations();
            for (Annotation annotation : a) {
                if (annotation instanceof Get) {
                    Get myAnnotation = (Get) annotation;
                    if (value.equals(myAnnotation.value()))
                        return m[i];
                }
            }

        }
        return null;
    }
}
