package com.luminia.bridge.network.packet.serializer;

import com.luminia.bridge.network.packet.BridgePacket;
import com.luminia.bridge.util.ByteBuffer;

public interface BridgePacketSerializer<T extends BridgePacket> {

    void serialize(ByteBuffer buffer, T packet);

    void deserialize(ByteBuffer buffer, T packet);
}
