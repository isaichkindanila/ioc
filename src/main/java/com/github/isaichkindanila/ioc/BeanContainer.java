package com.github.isaichkindanila.ioc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class BeanContainer {
    private final Collection<Object> beans;

    public static BeanContainer newInstance() {
        return new BeanContainer();
    }

    private BeanContainer() {
        beans = new ArrayList<>();
    }

    public void addBean(Object bean) {
        beans.add(bean);
    }

    Object[] getParameters(Class[] classes) {
        return Arrays.stream(classes)
                .map(this::getBean)
                .toArray(Object[]::new);
    }

    private void addBean(BeanInfo beanInfo) {
        beans.add(ClassUtils.createBean(this, beanInfo));
    }

    public void init() {
        var input = getClass().getResourceAsStream("/beans.json");

        if (input == null) {
            throw new IllegalStateException("file 'beans.json' not found in classpath");
        }

        var beanInfoList = BeanParser.parseBeans(input);
        var sortedInfoList = TopologicalSort.sorted(beanInfoList);

        sortedInfoList.forEach(this::addBean);
    }

    public <T> List<T> getBeans(Class<? extends T> beanClass) {
        return beans.stream()
                .filter(beanClass::isInstance)
                .map(beanClass::cast)
                .collect(Collectors.toList());
    }

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
        }

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
