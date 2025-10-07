package com.luminiadev.bridge.network.codec.packet.handler;

import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.BridgePacketDirection;

/**
 * The handler for handling one type of packet.
 *
 * @param <T> Packet type to handle
 * @see TypedBridgePacketHandler
 */
@FunctionalInterface
public interface BridgeSinglePacketHandler<T extends BridgePacket> {

    /**
     * Called when a packet is sent or received by the service.
     *
     * @param packet Packet object
     * @param direction Packet direction (from or to service)
     * @param serviceId Sender's service Id
     */
    void handle(T packet, BridgePacketDirection direction, String serviceId);
}
