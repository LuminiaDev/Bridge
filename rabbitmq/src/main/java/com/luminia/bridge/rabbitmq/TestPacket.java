package com.luminia.bridge.rabbitmq;

import com.luminia.bridge.network.packet.BridgePacket;
import com.luminia.bridge.network.packet.serializer.BridgePacketSerializer;
import com.luminia.bridge.network.packet.serializer.BridgePacketSerializerHelper;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TestPacket implements BridgePacket {

    public static final String ID = "test";

    private String value;

    @Override
    public String getId() {
        return ID;
    }

    public static class TestPacketSerializer implements BridgePacketSerializer<TestPacket> {

        @Override
        public void serialize(ByteBuf buffer, BridgePacketSerializerHelper helper, TestPacket packet) {
            helper.writeString(buffer, packet.getValue());
        }

        @Override
        public void deserialize(ByteBuf buffer, BridgePacketSerializerHelper helper, TestPacket packet) {
            packet.setValue(helper.readString(buffer));
        }
    }
}
