package com.luminia.bridge.network.packet;

import com.luminia.bridge.network.packet.serializer.BridgePacketSerializer;
import lombok.Value;

import java.util.function.Supplier;

@Value
public class BridgePacketDefinition<T extends BridgePacket> {
    String id;
    Supplier<T> factory;
    BridgePacketSerializer<T> serializer;
}
