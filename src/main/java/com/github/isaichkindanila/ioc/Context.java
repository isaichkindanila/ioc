package com.github.isaichkindanila.ioc;

import java.util.List;

/**
 * Interface all contexts must implement.
 * Contexts are responsible for finding and creating components.
 */
public interface Context {

    /**
     * Finds a component which can be casted to specified class.
     * If several components are found then the one with specified name is chosen.
     *
     * @param clazz component's superclass
     * @param name  component's name
     * @return component casted to specified class
     * @throws ComponentException if zero or several components with specified name can be casted to specified class
     */
    <T> T getComponent(Class<? extends T> clazz, String name);

    /**
     * Finds all components which can be casted to specified class.
     *
     * @param clazz components' superclass
     * @return {@code List} of components casted to specified class
     */
    <T> List<T> getComponents(Class<? extends T> clazz);

    /**
     * Finds a component which can be casted to specified class.
     *
     * @param clazz component's superclass
     * @return component casted to specified class
     * @throws ComponentException if zero or several components can be casted to specified class
     */
    default <T> T getComponent(Class<? extends T> clazz) {
        return getComponent(clazz, null);
    }
}
