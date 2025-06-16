package com.luminia.bridge.network;

import com.luminia.bridge.network.handler.BridgePacketHandler;
import com.luminia.bridge.network.packet.BridgePacket;
import com.luminia.bridge.network.packet.BridgePacketDefinition;
import com.luminia.bridge.network.packet.serializer.BridgePacketSerializerHelper;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Set;

public interface BridgeNetwork {

    void start();

    void close();

    @Nullable BridgePacketDefinition<? extends BridgePacket> getPacketDefinition(String id);

    <T extends BridgePacket> void registerPacket(BridgePacketDefinition<T> definition);

    void unregisterPacket(String packetId);

    @UnmodifiableView Set<BridgePacketHandler> getPacketHandlers();

    void addPacketHandler(BridgePacketHandler handler);

    void removePacketHandler(BridgePacketHandler handler);

    <T extends BridgePacket> void sendPacket(T packet);

    BridgePacket tryDecode(ByteBuf buffer, BridgePacketSerializerHelper helper, String packetId);

    <T extends BridgePacket> void tryEncode(ByteBuf buffer, BridgePacketSerializerHelper helper, T packet);
}
