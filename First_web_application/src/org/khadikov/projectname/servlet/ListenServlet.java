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
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.*;


import org.eclipse.jdt.internal.compiler.batch.ClasspathJar;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.khadikov.projectname.annotations.Restful;
import org.khadikov.projectname.dto.User;
import org.khadikov.projectname.rest.Users;
import org.khadikov.projectname.annotations.Path;

//@Path("/UserService")
public class ListenServlet extends HttpServlet {
    String path;
    Map<String,Class> handlers = new HashMap<String,Class>();
    public void init()throws ServletException
    {

        try {
            Class[] c = getClasses("org.khadikov.projectname");
            for (int i = 0; i < c.length; i++) {
                String name = c[i].getName();
                Class cl = Class.forName(name);
                Annotation[] a = cl.getAnnotations();
                for (int j = 0; j < a.length; j++)
                    if (a[j] instanceof Restful) {
                        Restful rest=(Restful) a[j];
                        handlers.put(rest.value(),cl);
                    }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        PrintWriter out=resp.getWriter();
        for (Map.Entry<String,Class> entry : handlers.entrySet()) {
            String key = entry.getKey();
            Class value = entry.getValue();
        }
        if(method.equals("GET")) {
            for (Map.Entry<String,Class> entry : handlers.entrySet()) {
                String key = entry.getKey();
                Class value = entry.getValue();
                if(key.equals(req.getRequestURI())){
                    try {
                        Method m=value.getMethod("get_all_users",null);
                        Object obj=value.newInstance();
                        m.invoke(obj, null);
                        out.println(obj.toString());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
        else if(method.equals("POST")) {

        } else if(method.equals("PUT")) {

        } else if(method.equals("DELETE")) {

        }
    }
    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
