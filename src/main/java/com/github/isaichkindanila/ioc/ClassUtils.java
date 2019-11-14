package com.github.isaichkindanila.ioc;

import java.util.Arrays;

class ClassUtils {
    private static final String CLASS_NOT_FOUND = "class %s not found";
    private static final String CREATION_FAILURE = "failed to create bean %s";

    private static RuntimeException fatal(String template, String parameter, Throwable cause) {
        return new ComponentException(String.format(template, parameter), cause);
    }

    static Class getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw fatal(CLASS_NOT_FOUND, className, e);
        }
    }

    private static Object[] getParameters(ComponentContainer container, Class[] argClasses) {
        return Arrays.stream(argClasses)
                .map(container::getComponent)
                .toArray();
    }

    @SuppressWarnings("unchecked")
    private static Object createBean0(ComponentContainer container, ComponentInfo info) throws Exception {
        var beanClass = info.getComponentClass();
        var argClasses = info.getArgClasses();

        var constructor = beanClass.getConstructor(argClasses);
        var parameters = getParameters(container, argClasses);

        constructor.setAccessible(true);
        return constructor.newInstance(parameters);
    }

    static Object createBean(ComponentContainer container, ComponentInfo info) {
        try {
            return createBean0(container, info);
        } catch (Exception e) {
            throw fatal(CREATION_FAILURE, info.getComponentClass().getName(), e);
        }
    }

    private ClassUtils() {}
}
