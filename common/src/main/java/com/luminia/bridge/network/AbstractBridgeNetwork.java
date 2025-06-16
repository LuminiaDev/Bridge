package com.luminia.bridge.network;

import com.luminia.bridge.network.handler.BridgePacketHandler;
import com.luminia.bridge.network.packet.BridgePacket;
import com.luminia.bridge.network.packet.BridgePacketDefinition;
import com.luminia.bridge.network.packet.serializer.BridgePacketSerializer;
import com.luminia.bridge.network.packet.serializer.BridgePacketSerializerHelper;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public abstract class AbstractBridgeNetwork implements BridgeNetwork {

    private final Map<String, BridgePacketDefinition<?>> packetsById = new HashMap<>();
    private final Set<BridgePacketHandler> handlers = new HashSet<>();

    @Override
    public @Nullable BridgePacketDefinition<?> getPacketDefinition(String id) {
        return packetsById.get(id);
    }

    @Override
    public <T extends BridgePacket> void registerPacket(BridgePacketDefinition<T> definition) {
        if (!packetsById.containsKey(definition.getId())) {
            packetsById.put(definition.getId(), definition);
        } else {
            throw new UnsupportedOperationException("Packet with id " + definition.getId() + " is already registered");
        }
    }

    @Override
    public void unregisterPacket(String packetId) {
        if (packetsById.remove(packetId) == null) {
            throw new UnsupportedOperationException("Packet with id " + packetId + " is not registered");
        }
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
    @SuppressWarnings("unchecked")
    public BridgePacket tryDecode(ByteBuf buffer, BridgePacketSerializerHelper helper, String packetId) {
        BridgePacketDefinition<? extends BridgePacket> definition = this.getPacketDefinition(packetId);

        BridgePacket packet;
        BridgePacketSerializer<BridgePacket> serializer;
        if (definition != null) {
            packet = definition.getFactory().get();
            serializer = (BridgePacketSerializer<BridgePacket>) definition.getSerializer();
        } else {
            throw new RuntimeException();
        }

        serializer.deserialize(buffer, helper, packet);
        return packet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BridgePacket> void tryEncode(ByteBuf buffer, BridgePacketSerializerHelper helper, T packet) {
        BridgePacketDefinition<T> definition = (BridgePacketDefinition<T>) this.getPacketDefinition(packet.getId());
        if (definition == null) {
            throw new RuntimeException();
        }
        BridgePacketSerializer<T> serializer = definition.getSerializer();
        serializer.serialize(buffer, helper, packet);
    }
}
