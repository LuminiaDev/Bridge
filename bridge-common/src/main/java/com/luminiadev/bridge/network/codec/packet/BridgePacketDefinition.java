package com.luminiadev.bridge.network.codec.packet;

import com.luminiadev.bridge.network.codec.packet.serializer.BridgePacketSerializer;
import lombok.Value;

/**
 * Bridge packet definition, contains id, factory and serializer of the packet.
 *
 * @param <T> Packet type
 */
@Value
public class BridgePacketDefinition<T extends BridgePacket> {
    String id;
    BridgePacketFactory<T> factory;
    BridgePacketSerializer<T> serializer;
}
