package com.luminia.bridge.network.codec.packet;

@FunctionalInterface
public interface BridgePacketFactory<T extends BridgePacket> {
    T create();
}
