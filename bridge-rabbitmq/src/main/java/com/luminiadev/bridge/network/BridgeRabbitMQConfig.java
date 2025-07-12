package com.luminiadev.bridge.network;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BridgeRabbitMQConfig {
    private final String host;
    @Builder.Default
    private final int port = -1; // -1 for use default value
    private final BridgeRabbitMQCredentials credentials;
    @Builder.Default
    private final String virtualHost = "/"; // default virtual host value
    @Builder.Default
    private final String serviceId = null; // default value for auto generate

    public BridgeRabbitMQConfig(String host, int port, BridgeRabbitMQCredentials credentials, String serviceId) {
        this(host, port, credentials, "/", serviceId);
    }

    public BridgeRabbitMQConfig(String host, int port, BridgeRabbitMQCredentials credentials, String virtualHost, String serviceId) {
        this.host = host;
        this.port = port;
        this.credentials = credentials;
        this.virtualHost = virtualHost;
        this.serviceId = serviceId;
    }
}
