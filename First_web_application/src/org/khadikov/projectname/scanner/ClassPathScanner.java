package org.khadikov.projectname.scanner;

import org.khadikov.projectname.annotations.Restful;
import java.io.IOException;
import java.io.File;
import java.lang.annotation.*;
import java.net.URL;
import java.util.*;

public class ClassPathScanner {
    Map<String, Class> handlers = new HashMap<String, Class>();

    public ClassPathScanner(String packageName) {
        try {
            Class[] c = getClasses(packageName);
            for (int i = 0; i < c.length; i++) {
                String name = c[i].getName();
                Class cl = Class.forName(name);
                Annotation[] a = cl.getAnnotations();
                for (int j = 0; j < a.length; j++)
                    if (a[j] instanceof Restful) {
                        Restful rest = (Restful) a[j];
                        handlers.put(rest.value(), cl);
                    }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Map<String, Class> getRestHandlers() {
        return handlers;
    }


    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
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