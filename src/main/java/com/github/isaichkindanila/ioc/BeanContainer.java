package com.github.isaichkindanila.ioc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for finding and creating beans.
 */
@SuppressWarnings("WeakerAccess")
public class BeanContainer {
    private final Collection<Object> beans;

    /**
     * Creates new instance of BeanContainer.
     * <p>
     * The instance would contain beans specified in {@code "beans.json"} file
     * (which is automatically created by annotation processor during compilation),
     * as well as beans passed as parameters to this method.
     *
     * @param beans base beans necessary to create annotated beans
     * @return new instance of BeanContainer
     * @throws BeanException if beans cannot be created for some reason
     */
    public static BeanContainer newInstance(Object... beans) {
        var instance = new BeanContainer();

        instance.beans.addAll(Arrays.asList(beans));
        instance.init();

        return instance;
    }

    private BeanContainer() {
        beans = new ArrayList<>();
    }

    private void addBean(BeanInfo beanInfo) {
        beans.add(ClassUtils.createBean(this, beanInfo));
    }

    private void init() {
        var input = getClass().getResourceAsStream("/beans.json");

        if (input == null) {
            throw new IllegalStateException("file 'beans.json' not found in classpath");
        }

        var beanInfoList = BeanParser.parseBeans(input);
        var sortedInfoList = TopologicalSort.sorted(beanInfoList);

        sortedInfoList.forEach(this::addBean);
    }

    /**
     * Finds all beans which can be casted to specified class.
     *
     * @param beanClass beans' superclass
     * @return list of beans casted to specified class
     */
    public <T> List<T> getBeans(Class<? extends T> beanClass) {
        return beans.stream()
                .filter(beanClass::isInstance)
                .map(beanClass::cast)
                .collect(Collectors.toList());
    }

    /**
     * Finds one bean which can be casted to specified class.
     * <p>
     * If zero or more than one bean is found, then {@code BeanException} is thrown.
     *
     * @param beanClass bean's superclass
     * @return bean casted to specified class
     * @throws BeanException if zero or more than one bean is found
     */
    @SuppressWarnings("StringBufferReplaceableByString")
    public <T> T getBean(Class<? extends T> beanClass) {
        var candidates = getBeans(beanClass);

        if (candidates.size() == 1) {
            // exactly 1 bean found - everything is OK
            return candidates.get(0);
        }

        if (candidates.size() == 0) {
            // no beans found - throw exception
            throw new BeanException("no beans found for class " + beanClass.getName());
        } else {
            // several beans found - throw exception
            var arrayString = candidates.stream()
                    .map(bean -> bean.getClass().getName())
                    .collect(Collectors.joining(", "));

            var builder = new StringBuilder()
                    .append("multiple beans found for class ")
                    .append(beanClass)
                    .append(": [")
                    .append(arrayString)
                    .append(']');

            throw new BeanException(builder.toString());
        }
    }
}
