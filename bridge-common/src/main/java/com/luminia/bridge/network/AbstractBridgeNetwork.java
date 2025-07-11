package com.luminia.bridge.network;

import com.luminia.bridge.exception.BridgeCodecException;
import com.luminia.bridge.network.codec.BridgeCodec;
import com.luminia.bridge.network.codec.packet.handler.BridgePacketHandler;
import com.luminia.bridge.network.codec.packet.BridgePacket;
import com.luminia.bridge.util.ByteBuffer;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

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
    public void addPacketHandler(BridgePacketHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void removePacketHandler(BridgePacketHandler handler) {
        handlers.remove(handler);
    }

    @Override
    public BridgePacket tryDecode(ByteBuffer buffer, String packetId) {
        if (codec == null) {
            throw new BridgeCodecException("Codec is not set");
        }
        return codec.tryDecode(buffer, packetId);
    }

    @Override
    public <T extends BridgePacket> void tryEncode(ByteBuffer buffer, T packet) {
        if (codec == null) {
            throw new BridgeCodecException("Codec is not set");
        }
        codec.tryEncode(buffer, packet);
    }
}