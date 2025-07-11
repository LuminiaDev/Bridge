package com.luminia.bridge.network;

import com.luminia.bridge.network.codec.BridgeCodec;
import com.luminia.bridge.network.codec.packet.handler.BridgePacketHandler;
import com.luminia.bridge.network.codec.packet.BridgePacket;
import com.luminia.bridge.util.ByteBuffer;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Set;

public interface BridgeNetwork {

    void start();

    void close();

    void setCodec(BridgeCodec codec);

    @Nullable BridgeCodec getCodec();

    @UnmodifiableView Set<BridgePacketHandler> getPacketHandlers();

    void addPacketHandler(BridgePacketHandler handler);

    void removePacketHandler(BridgePacketHandler handler);

    <T extends BridgePacket> void sendPacket(T packet);

    BridgePacket tryDecode(ByteBuffer buffer, String packetId);

    <T extends BridgePacket> void tryEncode(ByteBuffer buffer, T packet);
}