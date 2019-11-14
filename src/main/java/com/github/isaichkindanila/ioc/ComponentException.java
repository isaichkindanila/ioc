package com.github.isaichkindanila.ioc;

/**
 * Thrown if any problem with components occurs.
 */
@SuppressWarnings("WeakerAccess")
public class ComponentException extends RuntimeException {

    ComponentException(String message) {
        super(message);
    }

    ComponentException(String message, Throwable cause) {
        super(message, cause);
    }
}
