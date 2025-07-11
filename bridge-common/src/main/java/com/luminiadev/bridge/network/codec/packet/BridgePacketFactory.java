package com.luminiadev.bridge.network.codec.packet;

@FunctionalInterface
public interface BridgePacketFactory<T extends BridgePacket> {
    T create();
}
