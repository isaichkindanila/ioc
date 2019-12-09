package com.github.isaichkindanila.ioc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages components marked with {@link com.github.isaichkindanila.ioc.annotation.Component}.
 */
@SuppressWarnings("WeakerAccess")
public class ReflectionContext implements Context {
    private final Collection<ComponentWrapper> container;

    private ReflectionContext(ComponentWrapper... wrappers) {
        container = new ArrayList<>(Arrays.asList(wrappers));
    }

    /**
     * Creates new instance of {@code ReflectionContext}.
     *
     * @param basePackage    common package of all components
     * @param baseComponents base components required to create other components
     * @return new instance of {@code ComponentContainer}
     * @throws ComponentException if component creation is impossible,
     *                            e.g. circular dependencies found
     *                            or some component does not have a public constructor
     */
    public static ReflectionContext newInstance(String basePackage, Object... baseComponents) {
        var wrappers = Arrays.stream(baseComponents)
                .map(ComponentWrapper::new)
                .toArray(ComponentWrapper[]::new);

        return newInstance(basePackage, wrappers);
    }

    /**
     * Creates new instance of {@code ReflectionContext}.
     *
     * @param basePackage    common package of all components
     * @param baseComponents base components required to create other components
     * @return new instance of {@code ComponentContainer}
     * @throws ComponentException if component creation is impossible,
     *                            e.g. circular dependencies found
     *                            or some component does not have a public constructor
     */
    public static ReflectionContext newInstance(String basePackage, ComponentWrapper... baseComponents) {
        var instance = new ReflectionContext(baseComponents);

        var componentsInfoList = ComponentParser.parseFrom(basePackage);
        var sortedInfoList = TopologicalSort.sorted(componentsInfoList);

        sortedInfoList.forEach(instance::addComponent);

        return instance;
    }

    private void addComponent(ComponentInfo info) {
        container.add(ComponentFactory.createComponent(this, info));
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> getComponents(Class<? extends T> clazz) {
        return container.stream()
                .map(ComponentWrapper::getComponent)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private <T> T getComponent(Class<? extends T> clazz,
                               List<ComponentWrapper> candidates,
                               String definition,
                               boolean fatal) {
        if (candidates.size() == 0) {
            // no components found - throw exception
            throw new ComponentException("no components found for " + definition);
        }

        if (candidates.size() == 1) {
            // exactly 1 component found - everything is OK
            return clazz.cast(candidates.get(0).getComponent());
        }

        if (fatal) {
            // several components found - throw exception
            var arrayString = candidates.stream()
                    .map(wrapper -> wrapper.getClass().getName())
                    .collect(Collectors.joining(", "));

            var builder = new StringBuilder()
                    .append("several components found for ")
                    .append(definition)
                    .append(clazz)
                    .append(": [")
                    .append(arrayString)
                    .append(']');

            throw new ComponentException(builder.toString());
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> T getComponent(Class<? extends T> clazz, String name) {
        var candidates = container.stream()
                .filter(wrapper -> clazz.isInstance(wrapper.getComponent()))
                .collect(Collectors.toList());

        var result = getComponent(clazz, candidates, "class " + clazz.getName(), name == null);

        if (result == null) {
            // result is null but exception wasn't thrown -> name is not null
            assert name != null;

            candidates = candidates.stream()
                    .filter(wrapper -> name.equals(wrapper.getName()))
                    .collect(Collectors.toList());

            result = getComponent(clazz, candidates, "'" + name + "'", true);
        }

        return result;
    }

    Object getComponent(Parameter parameter) {
        return getComponent(parameter.getClazz(), parameter.getName());
    }
}
