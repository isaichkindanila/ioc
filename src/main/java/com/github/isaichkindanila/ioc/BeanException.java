package com.github.isaichkindanila.ioc;

@SuppressWarnings("WeakerAccess")
public class BeanException extends RuntimeException {

    BeanException(String message) {
        super(message);
    }

    BeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
