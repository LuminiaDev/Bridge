package com.luminiadev.bridge.network.codec.packet;

import org.jetbrains.annotations.NotNull;

/**
 * A bridge packet.
 */
public interface BridgePacket {

    /**
     * Gets packet id.
     *
     * @return Packet id
     */
    @NotNull String getId();
}
