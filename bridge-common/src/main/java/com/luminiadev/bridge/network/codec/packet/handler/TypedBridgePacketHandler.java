package com.luminiadev.bridge.network.codec.packet.handler;

import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.BridgePacketDirection;

/**
 * Internal implementation of handler to handle one type of packet.
 *
 * @param <T> Packet type to handle
 */
public final class TypedBridgePacketHandler<T extends BridgePacket> implements BridgePacketHandler {

    private final Class<T> packetClass;
    private final BridgeSinglePacketHandler<T> handler;

    public TypedBridgePacketHandler(Class<T> packetClass, BridgeSinglePacketHandler<T> handler) {
        this.packetClass = packetClass;
        this.handler = handler;
    }

    @Override
    public void handle(BridgePacket packet, BridgePacketDirection direction, String serviceId) {
        if (packetClass.isInstance(packet)) {
            handler.handle(packetClass.cast(packet), direction, serviceId);
        }
    }
}
