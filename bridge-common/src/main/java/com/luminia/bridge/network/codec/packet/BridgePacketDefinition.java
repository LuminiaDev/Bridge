package com.luminia.bridge.network.codec.packet;

import com.luminia.bridge.network.codec.packet.serializer.BridgePacketSerializer;
import lombok.Value;

@Value
public class BridgePacketDefinition<T extends BridgePacket> {
    String id;
    BridgePacketFactory<T> factory;
    BridgePacketSerializer<T> serializer;
}
