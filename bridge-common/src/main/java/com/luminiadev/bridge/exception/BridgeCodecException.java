package com.luminiadev.bridge.exception;

public class BridgeCodecException extends BridgeException {

    public BridgeCodecException(String message) {
        super(message);
    }

    public BridgeCodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public BridgeCodecException(Throwable cause) {
        super(cause);
    }
}
