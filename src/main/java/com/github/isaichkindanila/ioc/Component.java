package com.github.isaichkindanila.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks classes that should be instantiated as components.
 * <p>
 * Components must be <i>top-level</i>, <i>not abstract</i>
 * classes with <i>exactly one</i> public constructor.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}
