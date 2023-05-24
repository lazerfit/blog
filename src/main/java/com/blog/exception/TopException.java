package com.blog.exception;

public abstract class TopException extends RuntimeException{

    protected TopException(String message) {
        super(message);
    }

    protected TopException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
