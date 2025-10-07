package com.luminiadev.bridge.network;

import com.luminiadev.bridge.network.codec.BridgeCodec;
import com.luminiadev.bridge.network.codec.packet.BridgePacket;
import com.luminiadev.bridge.network.codec.packet.BridgePacketDirection;
import com.luminiadev.bridge.network.codec.packet.handler.BridgePacketHandler;
import com.luminiadev.bridge.network.codec.packet.serializer.BridgePacketSerializer;
import com.luminiadev.bridge.network.codec.packet.serializer.BridgePacketSerializerHelper;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Test {

        public static void main(String[] args) {
        BridgeNetwork bridge = new BridgeRabbitMQNetwork(BridgeRabbitMQConfig.builder()
                .credentials(new BridgeRabbitMQCredentials("guest", "guest"))
                .serviceId("service-1")
                .build());

        bridge.setCodec(BridgeCodec.builder()
                .registerPacket("message", MessagePacket::new, new MessagePacket.MessagePacketSerializer())
                .build());
        bridge.start();

        bridge.addPacketHandler(MessagePacket.class, (packet, direction, serviceId) -> {
            System.out.println(direction + " Received packet: " + packet);
        });

        MessagePacket packet = new MessagePacket();
        packet.setMessage("Message 1");
        packet.setUuid(UUID.randomUUID().toString());
        bridge.sendPacket(packet);
    }

    @Data
    public static class MessagePacket implements BridgePacket {

        private String message;
        private String uuid;

        @Override
        public @NotNull String getId() {
            return "message";
        }

        public static class MessagePacketSerializer implements BridgePacketSerializer<MessagePacket> {

            @Override
            public void serialize(ByteBuf buffer, BridgePacketSerializerHelper helper, MessagePacket packet) {
                helper.writeString(packet.getMessage());
                helper.writeString(packet.getUuid());
            }

            @Override
            public void deserialize(ByteBuf buffer, BridgePacketSerializerHelper helper, MessagePacket packet) {
                packet.setMessage(helper.readString());
                packet.setUuid(helper.readString());
            }
        }
    }
}
