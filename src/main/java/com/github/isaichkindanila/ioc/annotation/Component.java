package com.github.isaichkindanila.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks classes that should be instantiated as components.
 * <p>
 * Components must be <i>not abstract</i> classes with <i>exactly one</i> public constructor.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    /**
     * Used to distinguish between components of the same base class.
     *
     * @return component's name
     */
    String name() default "";
}
