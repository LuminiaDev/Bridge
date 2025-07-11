package com.luminia.bridge.network.codec.packet.handler;

import com.luminia.bridge.network.codec.packet.BridgePacket;
import com.luminia.bridge.network.codec.packet.BridgePacketDirection;

public interface BridgePacketHandler {
    void handle(BridgePacket packet, BridgePacketDirection direction);
}
