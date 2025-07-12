package com.luminiadev.bridge.network.codec.packet;

import com.luminiadev.bridge.network.codec.packet.serializer.BridgePacketSerializer;
import com.luminiadev.bridge.network.codec.packet.serializer.BridgePacketSerializerHelper;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

/**
 * Unknown packet class.
 * If an unregistered packet arrives to the codec, it will be handled as "BridgeUnknownPacket".
 */
@Data
@EqualsAndHashCode(doNotUseGetters = true)
public final class BridgeUnknownPacket implements BridgePacket {

    private String originalId;
    private ByteBuf originalPayload;

    @Override
    public @NotNull String getId() {
        return "unknown";
    }

    public static final class BridgeUnknownPacketSerializer implements BridgePacketSerializer<BridgeUnknownPacket> {

        @Override
        public void serialize(ByteBuf buffer, BridgePacketSerializerHelper helper, BridgeUnknownPacket packet) {
            helper.writeString(packet.originalId);
            buffer.writeBytes(packet.originalPayload, packet.originalPayload.readerIndex(), packet.originalPayload.readableBytes());
        }

        @Override
        public void deserialize(ByteBuf buffer, BridgePacketSerializerHelper helper, BridgeUnknownPacket packet) {
            packet.setOriginalId(helper.readString());
            packet.setOriginalPayload(buffer.readRetainedSlice(buffer.readableBytes()));
        }
    }
}
