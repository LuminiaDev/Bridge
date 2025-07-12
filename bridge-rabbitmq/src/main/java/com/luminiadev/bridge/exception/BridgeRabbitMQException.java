package com.luminiadev.bridge.exception;

/**
 * Custom exception for errors in the bridge implementation for RabbitMQ.
 */
public class BridgeRabbitMQException extends BridgeException {

    /**
     * Creates a new BridgeRabbitMQException with the specified message.
     *
     * @param message The detail message
     */
    public BridgeRabbitMQException(String message) {
        super(message);
    }

    /**
     * Creates a new BridgeRabbitMQException with the specified message and cause.
     *
     * @param message The detail message
     * @param cause The cause of the exception
     */
    public BridgeRabbitMQException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new BridgeRabbitMQException with the specified cause.
     *
     * @param cause The cause of the exception
     */
    public BridgeRabbitMQException(Throwable cause) {
        super(cause);
    }
}
