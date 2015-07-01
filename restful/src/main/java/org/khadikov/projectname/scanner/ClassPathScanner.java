package org.khadikov.projectname.scanner;

import org.khadikov.projectname.annotations.Restful;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

public class ClassPathScanner {
    Map<String, Class> handlers = new HashMap<String, Class>();
    Map<String , Object> instances=new HashMap<String, Object>();
    public ClassPathScanner(String packageName) {
        try {
            Class[] classes = getClasses(packageName);

            for (final Class aClass : classes) {
                Annotation[] annotations = aClass.getAnnotations();

                for (final Annotation annotation : annotations) {

                    if (annotation instanceof Restful) {
                        Restful rest = (Restful) annotation;
                        handlers.put(rest.value(), aClass);
                        instances.put(rest.value(),aClass.newInstance());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    public Map<String,Object> getRestInstances(){return instances;}
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

        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }
}