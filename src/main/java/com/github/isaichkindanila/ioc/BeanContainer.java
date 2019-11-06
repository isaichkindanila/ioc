package com.github.isaichkindanila.ioc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

public class BeanContainer {
    private static final String CLASS_NOT_FOUND = "class %s not found";
    private static final String CONSTRUCTOR_NOT_FOUND = "constructor not found for %s";
    private static final String ILLEGAL_ACCESS = "cannot access constructor of %s";
    private static final String CREATION_FAILURE = "failed to create bean %s";

    private final Collection<Object> beans;

    public static BeanContainer newInstance() {
        return new BeanContainer();
    }

    private static RuntimeException fatal(String template, String parameter, Throwable cause) {
        return new IllegalStateException(String.format(template, parameter), cause);
    }

    private BeanContainer() {
        beans = new ArrayList<>();
    }

    public void addBean(Object bean) {
        beans.add(bean);
    }

    private Class getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw fatal(CLASS_NOT_FOUND, className, e);
        }
    }

    private Class[] getArgClasses(JSONArray args) {
        return args.toList().stream()
                .map(object -> (String) object)
                .map(this::getClass)
                .toArray(Class[]::new);
    }

    private Object[] getParameters(Class[] classes) {
        return new Object[0];
    }

    @SuppressWarnings("unchecked")
    private void readBean(JSONObject beanInfo) {
        var className = beanInfo.getString("class");
        var argClassNames = beanInfo.getJSONArray("args");

        var beanClass = getClass(beanInfo.getString("class"));
        var argClasses = getArgClasses(argClassNames);

        try {
            var constructor = beanClass.getDeclaredConstructor(argClasses);
            var parameters = getParameters(argClasses);
            var bean = constructor.newInstance(parameters);

            beans.add(bean);
        } catch (NoSuchMethodException e) {
            throw fatal(CONSTRUCTOR_NOT_FOUND, className, e);
        } catch (IllegalAccessException e) {
            throw fatal(ILLEGAL_ACCESS, className, e);
        } catch (InstantiationException | InvocationTargetException e) {
            throw fatal(CREATION_FAILURE, className, e);
        }
    }

    public void readBeansFrom(InputStream input) {
        var jsonString = IOUtils.readAll(input);
        var beanArray = new JSONArray(jsonString);

        for (int i = 0; i < beanArray.length(); i++) {
            var beanInfo = beanArray.getJSONObject(i);
            readBean(beanInfo);
        }
    }

    public void readBeansFromClasspath() {
        var input = getClass().getResourceAsStream("/beans.json");

        if (input == null) {
            throw new IllegalStateException("file 'beans.json' not found in classpath");
        }

        readBeansFrom(input);
    }

    public <T> T getBean(Class<? extends T> beanClass) {
        return null;
    }
}
