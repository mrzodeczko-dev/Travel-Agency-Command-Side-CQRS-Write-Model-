package com.rzodeczko.domain.exception;

/** Ensures transaction rollback on insufficient capacity. */
public class OverbookingException extends RuntimeException {
    public OverbookingException(String message) {
        super(message);
    }
}
