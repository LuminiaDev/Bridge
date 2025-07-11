package com.luminiadev.bridge.exception;

public class BridgeRabbitMQException extends BridgeException {

    public BridgeRabbitMQException(String message) {
        super(message);
    }

    public BridgeRabbitMQException(String message, Throwable cause) {
        super(message, cause);
    }

    public BridgeRabbitMQException(Throwable cause) {
        super(cause);
    }
}
