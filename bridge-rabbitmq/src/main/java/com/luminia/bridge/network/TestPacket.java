package com.luminia.bridge.network;

import com.luminia.bridge.network.codec.packet.BridgePacket;
import com.luminia.bridge.network.codec.packet.serializer.BridgePacketSerializer;
import com.luminia.bridge.util.ByteBuffer;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class TestPacket implements BridgePacket {

    public static final String IDENTIFIER = "test";

    private String[] texts;

    @Override
    public @NotNull String getId() {
        return IDENTIFIER;
    }

    public static class TestPacketSerializer implements BridgePacketSerializer<TestPacket> {

        @Override
        public void serialize(ByteBuffer buffer, TestPacket packet) {
            buffer.writeArray(packet.getTexts(), buffer::writeString);
        }

        @Override
        public void deserialize(ByteBuffer buffer, TestPacket packet) {
            List<String> texts = new ArrayList<>();
            buffer.readArray(texts, buffer::readString);
            packet.setTexts(texts.toArray(new String[0]));
        }
    }
}
