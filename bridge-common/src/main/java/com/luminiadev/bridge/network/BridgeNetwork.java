package com.luminiadev.bridge.network;

import com.luminiadev.bridge.network.codec.BridgeCodec;
import com.luminiadev.bridge.network.codec.packet.handler.BridgePacketHandler;
import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.util.ByteBuffer;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Set;

public interface BridgeNetwork {

    String getServiceId();

    void setCodec(BridgeCodec codec);

    @Nullable BridgeCodec getCodec();

    @UnmodifiableView Set<BridgePacketHandler> getPacketHandlers();

    void addPacketHandler(BridgePacketHandler handler);

    void removePacketHandler(BridgePacketHandler handler);

    <T extends BridgePacket> void sendPacket(T packet);

    BridgePacket tryDecode(ByteBuffer buffer, String packetId);

    <T extends BridgePacket> void tryEncode(ByteBuffer buffer, T packet);

    void start();

    void close();
}