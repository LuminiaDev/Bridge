package com.luminiadev.bridge.network.codec.packet.handler;

import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.BridgePacketDirection;

/**
 * Bridge packet handler.
 */
@FunctionalInterface
public interface BridgePacketHandler {

    /**
     * Called when a packet is sent or received by the service.
     *
     * @param packet Packet object
     * @param direction Packet direction (from or to service)
     * @param serviceId Sender's service Id
     */
    void handle(BridgePacket packet, BridgePacketDirection direction, String serviceId);
}
