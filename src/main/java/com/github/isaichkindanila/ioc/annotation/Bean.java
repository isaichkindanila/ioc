package com.github.isaichkindanila.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface, indicates that annotated class (or constructor)
 * should be used to create a bean.
 * <p>
 * If constructor is annotated, then it is used to create a bean.
 * <p>
 * If class is annotated, then public constructor is used to create a bean.
 * If there are multiple public constructors available, the constructor
 * with no arguments is used.
 */
@Target({ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Bean {
}
