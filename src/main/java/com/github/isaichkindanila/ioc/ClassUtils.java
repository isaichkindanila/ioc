package com.github.isaichkindanila.ioc;

import java.lang.reflect.InvocationTargetException;

class ClassUtils {
    private static final String CLASS_NOT_FOUND = "class %s not found";
    private static final String CONSTRUCTOR_NOT_FOUND = "constructor not found for %s";
    private static final String ILLEGAL_ACCESS = "cannot access constructor of %s";
    private static final String CREATION_FAILURE = "failed to create bean %s";

    private static RuntimeException fatal(String template, String parameter, Throwable cause) {
        return new BeanException(String.format(template, parameter), cause);
    }

    static Class getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw fatal(CLASS_NOT_FOUND, className, e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Object createBean0(BeanContainer container, BeanInfo info) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var beanClass = info.getBeanClass();
        var argClasses = info.getArgClasses();

        var constructor = beanClass.getDeclaredConstructor(argClasses);
        var parameters = container.getParameters(argClasses);

        return constructor.newInstance(parameters);
    }

    static Object createBean(BeanContainer container, BeanInfo info) {
        try {
            return createBean0(container, info);
        } catch (NoSuchMethodException e) {
            throw fatal(CONSTRUCTOR_NOT_FOUND, info.getBeanClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw fatal(ILLEGAL_ACCESS, info.getBeanClass().getName(), e);
        } catch (InstantiationException | InvocationTargetException e) {
            throw fatal(CREATION_FAILURE, info.getBeanClass().getName(), e);
        }
    }

    private ClassUtils() {}
}
