package com.luminiadev.bridge.network.codec.packet.serializer;

import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import io.netty.buffer.ByteBuf;

public interface BridgePacketSerializer<T extends BridgePacket> {

    void serialize(ByteBuf buffer, BridgePacketSerializerHelper helper, T packet);

    void deserialize(ByteBuf buffer, BridgePacketSerializerHelper helper, T packet);
}
