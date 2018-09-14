package com.johnli.callback.common;

/**
 * @author johnli  2018-08-08 16:03
 */
public class CallbackException extends RuntimeException{
    public CallbackException(String message) {
        super(message);
    }

    public CallbackException(String message, Throwable cause) {
        super(message, cause);
    }
}
