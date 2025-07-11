package com.luminiadev.bridge.exception;

public class BridgeException extends RuntimeException {

    public BridgeException(String message) {
        super(message);
    }

    public BridgeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BridgeException(Throwable cause) {
        super(cause);
    }
}
