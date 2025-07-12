package com.luminiadev.bridge.network.codec;

import com.luminiadev.bridge.exception.BridgeCodecException;
import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.BridgePacketDefinition;
import com.luminiadev.bridge.network.codec.packet.BridgePacketFactory;
import com.luminiadev.bridge.network.codec.packet.BridgeUnknownPacket;
import com.luminiadev.bridge.network.codec.packet.serializer.BridgePacketSerializer;
import com.luminiadev.bridge.network.codec.packet.serializer.BridgePacketSerializerHelper;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Bridge packet codec for registering and encoding packets.
 */
public final class BridgeCodec {

    private final Map<String, BridgePacketDefinition<? extends BridgePacket>> packetsById = new HashMap<>();
    private final Map<Class<?>, BridgePacketDefinition<? extends BridgePacket>> packetsByClass = new HashMap<>();

    /**
     * Creates an empty codec builder.
     *
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets the bridge packet definition by the id.
     *
     * @param id Packet id
     * @return BridgePacketDefinition or null
     */
    public @Nullable BridgePacketDefinition<? extends BridgePacket> getPacketDefinition(String id) {
        return packetsById.get(id);
    }

    /**
     * Gets the bridge packet definition by the id.
     * @param classOf Packet class
     * @param <T> Packet type
     * @return BridgePacketDefinition or null
     */
    @SuppressWarnings("unchecked")
    public @Nullable <T extends BridgePacket> BridgePacketDefinition<T> getPacketDefinition(Class<T> classOf) {
        return (BridgePacketDefinition<T>) packetsByClass.get(classOf);
    }

    /**
     * Register the packet definition.
     *
     * @param definition BridgePacketDefinition
     * @param <T> Packet type
     * @throws BridgeCodecException If the packet is already registered
     */
    public <T extends BridgePacket> void registerPacket(BridgePacketDefinition<T> definition) {
        if (!packetsById.containsKey(definition.getId())) {
            packetsById.put(definition.getId(), definition);
            packetsByClass.put(definition.getFactory().getClass(), definition);
        } else {
            throw new BridgeCodecException("Packet with id " + definition.getId() + " is already registered");
        }
    }

    /**
     * Deregister packet definition by packet id.
     *
     * @param packetId Packet id
     * @throws BridgeCodecException If the packet is not registered
     */
    public void unregisterPacket(String packetId) {
        BridgePacketDefinition<? extends BridgePacket> definition = packetsById.remove(packetId);
        if (definition != null) {
            packetsByClass.remove(definition.getClass());
        } else {
            throw new BridgeCodecException("Packet with id " + packetId + " is not registered");
        }
    }

    /**
     * Decodes the byte buffer in BridgePacket.
     *
     * @param buffer   Netty ByteBuf
     * @param packetId Packet id
     * @return The decoded BridgePacket
     * @throws BridgeCodecException If the codec is not set
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BridgePacket tryDecode(ByteBuf buffer, String packetId) {
        BridgePacketDefinition<? extends BridgePacket> definition = this.getPacketDefinition(packetId);
        BridgePacketSerializerHelper helper = new BridgePacketSerializerHelper(buffer);

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

        serializer.deserialize(buffer, helper, packet);
        return packet;
    }

    /**
     * Encodes the BridgePacket to byte buffer.
     *
     * @param buffer Netty ByteBuf
     * @param packet Packet object
     * @throws BridgeCodecException If the codec is not set
     */
    @SuppressWarnings("unchecked")
    public <T extends BridgePacket> void tryEncode(ByteBuf buffer, T packet) {
        BridgePacketSerializerHelper helper = new BridgePacketSerializerHelper(buffer);

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

        serializer.serialize(buffer, helper, packet);
    }

    /**
     * Creates Builder from codec with all codec parameters.
     *
     * @return Builder
     */
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
