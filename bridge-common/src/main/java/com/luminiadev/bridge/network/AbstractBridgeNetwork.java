package com.luminiadev.bridge.network;

import com.luminiadev.bridge.exception.BridgeCodecException;
import com.luminiadev.bridge.network.codec.BridgeCodec;
import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.handler.BridgePacketHandler;
import com.luminiadev.bridge.network.codec.packet.handler.BridgeSinglePacketHandler;
import com.luminiadev.bridge.network.codec.packet.handler.TypedBridgePacketHandler;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Bridge network interface abstract class with basic methods implementation.
 */
public abstract class AbstractBridgeNetwork implements BridgeNetwork {

    private BridgeCodec codec;
    private final Set<BridgePacketHandler> handlers = new HashSet<>();

    @Override
    public void setCodec(BridgeCodec codec) {
        this.codec = codec;
    }

    @Override
    public @Nullable BridgeCodec getCodec() {
        return codec;
    }

    @Override
    public @UnmodifiableView Set<BridgePacketHandler> getPacketHandlers() {
        return Collections.unmodifiableSet(handlers);
    }

    @Override
    public BridgePacketHandler addPacketHandler(BridgePacketHandler handler) {
        handlers.add(handler);
        return handler;
    }

    @Override
    public <T extends BridgePacket> BridgePacketHandler addPacketHandler(Class<T> packetClass, BridgeSinglePacketHandler<T> handler) {
        return this.addPacketHandler(new TypedBridgePacketHandler<>(packetClass, handler));
    }

    @Override
    public void removePacketHandler(BridgePacketHandler handler) {
        handlers.remove(handler);
    }

    @Override
    public BridgePacket tryDecode(ByteBuf buffer, String packetId) {
        if (codec == null) {
            throw new BridgeCodecException("Codec is not set");
        }
        return codec.tryDecode(buffer, packetId);
    }

    @Override
    public <T extends BridgePacket> void tryEncode(ByteBuf buffer, T packet) {
        if (codec == null) {
            throw new BridgeCodecException("Codec is not set");
        }
        codec.tryEncode(buffer, packet);
    }
}