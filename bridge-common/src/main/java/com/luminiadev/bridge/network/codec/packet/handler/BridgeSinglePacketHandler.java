package com.luminiadev.bridge.network.codec.packet.handler;

import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.BridgePacketDirection;

@FunctionalInterface
public interface BridgeSinglePacketHandler<T extends BridgePacket> {
    void handle(T packet, BridgePacketDirection direction, String serviceId);
}
