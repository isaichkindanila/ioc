package com.github.isaichkindanila.ioc;

/**
 * Thrown if any problem with beans occurs.
 */
@SuppressWarnings("WeakerAccess")
public class BeanException extends RuntimeException {

    BeanException(String message) {
        super(message);
    }

    BeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
