package com.github.isaichkindanila.ioc;

class ClassUtils {
    private static final String CLASS_NOT_FOUND = "class %s not found";
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
    private static Object createBean0(BeanContainer container, BeanInfo info) throws Exception {
        var beanClass = info.getBeanClass();
        var argClasses = info.getArgClasses();

        var constructor = beanClass.getConstructor(argClasses);
        var parameters = container.getParameters(argClasses);

        constructor.setAccessible(true);
        return constructor.newInstance(parameters);
    }

    static Object createBean(BeanContainer container, BeanInfo info) {
        try {
            return createBean0(container, info);
        } catch (Exception e) {
            throw fatal(CREATION_FAILURE, info.getBeanClass().getName(), e);
        }
    }

    private ClassUtils() {}
}
