package com.luminiadev.bridge.network.codec.packet;

/**
 * Bridge packet direction.
 */
public enum BridgePacketDirection {
    /**
     * The packet sent by the current service to other services.
     */
    FROM_SERVICE,
    /**
     * The packet was sent from another service to the current service.
     */
    TO_SERVICE
}
