package com.example.sweetandkarak.exception;

public class CafeNotApprovedException extends RuntimeException {
    public CafeNotApprovedException(String message) {
        super(message);
    }
}
