package com.luminiadev.bridge.network.codec.packet.handler;

import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.BridgePacketDirection;

public final class TypedBridgePacketHandler<T extends BridgePacket> implements BridgePacketHandler {

    private final Class<T> type;
    private final BridgeSinglePacketHandler<T> handler;

    public TypedBridgePacketHandler(Class<T> type, BridgeSinglePacketHandler<T> handler) {
        this.type = type;
        this.handler = handler;
    }

    @Override
    public void handle(BridgePacket packet, BridgePacketDirection direction, String serviceId) {
        if (type.isInstance(packet)) {
            handler.handle(type.cast(packet), direction, serviceId);
        }
    }
}
