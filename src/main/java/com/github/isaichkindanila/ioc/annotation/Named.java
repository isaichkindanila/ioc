package com.github.isaichkindanila.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks component's constructor parameters to specify that named component should be injected.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Named {
    /**
     * @return name of component that should be injected
     */
    String value();
}
