package com.luminiadev.bridge.exception;

/**
 * Custom exception for bridge errors.
 */
public class BridgeException extends RuntimeException {

    /**
     * Creates a new BridgeException with the specified message.
     *
     * @param message The detail message
     */
    public BridgeException(String message) {
        super(message);
    }

    /**
     * Creates a new BridgeException with the specified message and cause.
     *
     * @param message The detail message
     * @param cause The cause of the exception
     */
    public BridgeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new BridgeException with the specified cause.
     *
     * @param cause The cause of the exception
     */
    public BridgeException(Throwable cause) {
        super(cause);
    }
}
