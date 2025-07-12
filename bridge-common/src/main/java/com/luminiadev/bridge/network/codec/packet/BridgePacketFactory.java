package com.luminiadev.bridge.network.codec.packet;

/**
 * Bridge packet factory.
 *
 * @param <T> Packet type
 */
@FunctionalInterface
public interface BridgePacketFactory<T extends BridgePacket> {

    /**
     * Creates a new bridge packet instance.
     *
     * @return A new bridge packet instance
     */
    T create();
}
