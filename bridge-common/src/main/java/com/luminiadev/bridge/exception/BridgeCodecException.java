package com.luminiadev.bridge.exception;

/**
 * Custom exception for bridge codec errors.
 */
public class BridgeCodecException extends BridgeException {

    /**
     * Creates a new BridgeCodecException with the specified message.
     *
     * @param message The detail message
     */
    public BridgeCodecException(String message) {
        super(message);
    }

    /**
     * Creates a new BridgeCodecException with the specified message and cause.
     *
     * @param message The detail message
     * @param cause The cause of the exception
     */
    public BridgeCodecException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new BridgeCodecException with the specified cause.
     *
     * @param cause The cause of the exception
     */
    public BridgeCodecException(Throwable cause) {
        super(cause);
    }
}
