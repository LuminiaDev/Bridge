package com.luminiadev.bridge.network.codec;

import com.luminiadev.bridge.exception.BridgeCodecException;
import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.BridgePacketDefinition;
import com.luminiadev.bridge.network.codec.packet.BridgePacketFactory;
import com.luminiadev.bridge.network.codec.packet.BridgeUnknownPacket;
import com.luminiadev.bridge.network.codec.packet.serializer.BridgePacketSerializer;
import com.luminiadev.bridge.util.ByteBuffer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class BridgeCodec {

    private final Map<String, BridgePacketDefinition<? extends BridgePacket>> packetsById = new HashMap<>();
    private final Map<Class<?>, BridgePacketDefinition<? extends BridgePacket>> packetsByClass = new HashMap<>();

    public static Builder builder() {
        return new Builder();
    }

    public @Nullable BridgePacketDefinition<? extends BridgePacket> getPacketDefinition(String id) {
        return packetsById.get(id);
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T extends BridgePacket> BridgePacketDefinition<T> getPacketDefinition(Class<T> classOf) {
        return (BridgePacketDefinition<T>) packetsByClass.get(classOf);
    }

    public <T extends BridgePacket> void registerPacket(BridgePacketDefinition<T> definition) {
        if (!packetsById.containsKey(definition.getId())) {
            packetsById.put(definition.getId(), definition);
            packetsByClass.put(definition.getClass(), definition);
        } else {
            throw new BridgeCodecException("Packet with id " + definition.getId() + " is already registered");
        }
    }

    public void unregisterPacket(String packetId) {
        BridgePacketDefinition<? extends BridgePacket> definition = packetsById.remove(packetId);
        if (definition != null) {
            packetsByClass.remove(definition.getClass());
        } else {
            throw new BridgeCodecException("Packet with id " + packetId + " is not registered");
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public BridgePacket tryDecode(ByteBuffer buffer, String packetId) {
        BridgePacketDefinition<? extends BridgePacket> definition = this.getPacketDefinition(packetId);

        BridgePacket packet;
        BridgePacketSerializer<BridgePacket> serializer;
        if (definition != null) {
            packet = definition.getFactory().create();
            serializer = (BridgePacketSerializer<BridgePacket>) definition.getSerializer();
        } else {
            BridgeUnknownPacket unknownPacket = new BridgeUnknownPacket();
            unknownPacket.setOriginalId(packetId);
            unknownPacket.setOriginalPayload(buffer);
            packet = unknownPacket;
            serializer = (BridgePacketSerializer) new BridgeUnknownPacket.BridgeUnknownPacketSerializer();
        }

        serializer.deserialize(buffer, packet);
        return packet;
    }

    @SuppressWarnings("unchecked")
    public <T extends BridgePacket> void tryEncode(ByteBuffer buffer, T packet) {
        BridgePacketSerializer<T> serializer;
        if (packet instanceof BridgeUnknownPacket) {
            serializer = (BridgePacketSerializer<T>) new BridgeUnknownPacket.BridgeUnknownPacketSerializer();
        } else {
            BridgePacketDefinition<T> definition = (BridgePacketDefinition<T>) this.getPacketDefinition(packet.getId());
            if (definition == null) {
                throw new BridgeCodecException("Definition for packet with id " + packet.getId() + " not found");
            }
            serializer = definition.getSerializer();
        }
        serializer.serialize(buffer, packet);
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.definitions.putAll(packetsById);
        return builder;
    }

    public static class Builder {
        private final Map<String, BridgePacketDefinition<? extends BridgePacket>> definitions = new HashMap<>();

        public <T extends BridgePacket> Builder registerPacket(String id, BridgePacketFactory<T> factory, BridgePacketSerializer<T> serializer) {
            return registerPacket(new BridgePacketDefinition<>(id, factory, serializer));
        }

        public <T extends BridgePacket> Builder registerPacket(BridgePacketDefinition<T> definition) {
            if (!this.definitions.containsKey(definition.getId())) {
                this.definitions.put(definition.getId(), definition);
            } else {
                throw new BridgeCodecException("Packet with id " + definition.getId() + " is already registered");
            }
            return this;
        }

        public Builder unregisterPacket(String packetId) {
            if (definitions.remove(packetId) == null) {
                throw new BridgeCodecException("Packet with id " + packetId + " is not registered");
            }
            return this;
        }

        public BridgeCodec build() {
            BridgeCodec codec = new BridgeCodec();
            for (BridgePacketDefinition<? extends BridgePacket> definition : definitions.values()) {
                codec.registerPacket(definition);
            }
            return codec;
        }
    }
}
