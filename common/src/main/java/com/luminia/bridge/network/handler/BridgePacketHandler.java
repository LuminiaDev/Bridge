package com.luminia.bridge.network.handler;

import com.luminia.bridge.network.packet.BridgePacket;
import com.luminia.bridge.network.packet.BridgePacketDirection;

public interface BridgePacketHandler {
    void handle(BridgePacket packet, BridgePacketDirection direction);
}
