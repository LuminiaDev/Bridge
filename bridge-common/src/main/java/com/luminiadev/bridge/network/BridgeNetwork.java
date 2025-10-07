package com.luminiadev.bridge.network;

import com.luminiadev.bridge.exception.BridgeCodecException;
import com.luminiadev.bridge.network.codec.BridgeCodec;
import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.handler.BridgePacketHandler;
import com.luminiadev.bridge.network.codec.packet.handler.BridgeSinglePacketHandler;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Set;

/**
 * Bridge network interface.
 */
public interface BridgeNetwork {

    /**
     * Get service id.
     *
     * @return Service id
     */
    @NotNull String getServiceId();

    /**
     * Set up codec.
     *
     * @param codec The BridgeCodec
     */
    void setCodec(BridgeCodec codec);

    /**
     * Get set codec.
     *
     * @return Set codec or null, if not set
     */
    @Nullable BridgeCodec getCodec();

    /**
     * Get an immutable set of packet handlers.
     *
     * @return Set of packet handlers
     */
    @UnmodifiableView
    Set<BridgePacketHandler> getPacketHandlers();

    /**
     * Add a packet handler.
     *
     * @param handler The BridgePacketHandler
     */
    BridgePacketHandler addPacketHandler(BridgePacketHandler handler);

    /**
     * Add a packet handler.
     *
     * @param handler The BridgePacketHandler
     */
    <T extends BridgePacket> BridgePacketHandler addPacketHandler(Class<T> packetClass, BridgeSinglePacketHandler<T> handler);

    /**
     * Remove a packet handler.
     *
     * @param handler The BridgePacketHandler
     */
    void removePacketHandler(BridgePacketHandler handler);

    /**
     * Send a bridge packet.
     *
     * @param packet The BridgePacket
     * @param <T>    The packet type
     */
    <T extends BridgePacket> void sendPacket(T packet);

    /**
     * Decodes the byte buffer in BridgePacket.
     *
     * @param buffer   Netty ByteBuf
     * @param packetId Packet id
     * @return The decoded BridgePacket
     * @throws BridgeCodecException If the codec is not set
     */
    BridgePacket tryDecode(ByteBuf buffer, String packetId);

    /**
     * Encodes the BridgePacket to byte buffer.
     *
     * @param buffer Netty ByteBuf
     * @param packet Packet object
     * @throws BridgeCodecException If the codec is not set
     */
    <T extends BridgePacket> void tryEncode(ByteBuf buffer, T packet);

    /**
     * Starts the Bridge network interface.
     */
    void start();

    /**
     * Closes the Bridge network interface.
     */
    void close();
}