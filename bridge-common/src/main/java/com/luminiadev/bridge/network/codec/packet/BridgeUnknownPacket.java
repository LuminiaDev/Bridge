package com.luminiadev.bridge.network.codec.packet;

import com.luminiadev.bridge.network.codec.packet.serializer.BridgePacketSerializer;
import com.luminiadev.bridge.util.ByteBuffer;
import org.jetbrains.annotations.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(doNotUseGetters = true)
public final class BridgeUnknownPacket implements BridgePacket {

    private String originalId;
    private ByteBuffer originalPayload;

    @Override
    public @NotNull String getId() {
        return "unknown";
    }

    public static final class BridgeUnknownPacketSerializer implements BridgePacketSerializer<BridgeUnknownPacket> {

        @Override
        public void serialize(ByteBuffer buffer, BridgeUnknownPacket packet) {
            buffer.writeString(packet.getOriginalId());
            buffer.writeBytes(
                    packet.getOriginalPayload().byteBuf(),
                    packet.getOriginalPayload().readerIndex(),
                    packet.getOriginalPayload().readableBytes()
            );
        }

        @Override
        public void deserialize(ByteBuffer buffer, BridgeUnknownPacket packet) {
            packet.setOriginalId(buffer.readString());
            packet.setOriginalPayload(ByteBuffer.of(buffer.readRetainedSlice(buffer.readableBytes())));
        }
    }
}
