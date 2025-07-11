package com.luminiadev.bridge.network;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class BridgeRabbitMQConfig {
    private final String host;
    @Builder.Default
    private final int port = -1;
    private final BridgeRabbitMQCredentials credentials;
    @Builder.Default
    private final String virtualHost = "/";

    public BridgeRabbitMQConfig(String host, int port, BridgeRabbitMQCredentials credentials) {
        this(host, port, credentials, "/");
    }

    public BridgeRabbitMQConfig(String host, int port, BridgeRabbitMQCredentials credentials, String virtualHost) {
        this.host = host;
        this.port = port;
        this.credentials = credentials;
        this.virtualHost = virtualHost;
    }
}
