package com.luminiadev.bridge.network.codec.packet.handler;

import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.BridgePacketDirection;

public interface BridgePacketHandler {
    void handle(BridgePacket packet, BridgePacketDirection direction, String serviceId);
}
