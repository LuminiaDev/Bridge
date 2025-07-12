package com.luminiadev.bridge.network.codec.packet.serializer;

import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import io.netty.buffer.ByteBuf;

/**
 * Bridge packet serializer.
 *
 * @param <T>
 */
public interface BridgePacketSerializer<T extends BridgePacket> {

    /**
     * Serializes the packet into a byte buffer.
     *
     * @param buffer Netty ByteBuf
     * @param helper Packet serializer helper
     * @param packet Packet object
     */
    void serialize(ByteBuf buffer, BridgePacketSerializerHelper helper, T packet);

    /**
     * Deserializes the byte buffer into a packet.
     *
     * @param buffer Netty ByteBuf
     * @param helper Packet serializer helper
     * @param packet Packet object
     */
    void deserialize(ByteBuf buffer, BridgePacketSerializerHelper helper, T packet);
}
