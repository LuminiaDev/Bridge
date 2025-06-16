package com.luminia.bridge.network.packet.serializer;

import com.luminia.bridge.network.packet.BridgePacket;
import io.netty.buffer.ByteBuf;

public interface BridgePacketSerializer<T extends BridgePacket> {

    void serialize(ByteBuf buffer, BridgePacketSerializerHelper helper, T packet);

    void deserialize(ByteBuf buffer, BridgePacketSerializerHelper helper, T packet);
}
