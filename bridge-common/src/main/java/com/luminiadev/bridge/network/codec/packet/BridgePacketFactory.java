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

    /**
     * Gets the class of the packet.
     *
     * @return Class<T>
     */
    @SuppressWarnings("unchecked")
    default Class<T> getPacketClass() {
        return (Class<T>) this.create().getClass();
    }
}
