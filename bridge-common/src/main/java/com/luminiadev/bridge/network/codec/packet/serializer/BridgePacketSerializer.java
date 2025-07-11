package com.luminiadev.bridge.network.codec.packet.serializer;

import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.util.ByteBuffer;

public interface BridgePacketSerializer<T extends BridgePacket> {

    void serialize(ByteBuffer buffer, T packet);

    void deserialize(ByteBuffer buffer, T packet);
}
