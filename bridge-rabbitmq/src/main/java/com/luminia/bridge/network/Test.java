package com.luminia.bridge.network;

import com.luminia.bridge.network.codec.BridgeCodec;
import com.luminia.bridge.network.codec.packet.BridgePacketDirection;

public class Test {

    public static void main(String[] args) {
        BridgeCodec codec = BridgeCodec.builder()
                .registerPacket(TestPacket.IDENTIFIER, TestPacket::new, new TestPacket.TestPacketSerializer())
                .build();

        BridgeRabbitMQConfig config = BridgeRabbitMQConfig.builder()
                .host("localhost")
                .credentials(new BridgeRabbitMQCredentials("guest", "guest"))
                .build();
        BridgeNetwork network1 = new BridgeRabbitMQNetwork(config);
        BridgeNetwork network2 = new BridgeRabbitMQNetwork(config);

        network1.setCodec(codec);
        network1.start();
        network2.setCodec(codec);
        network2.start();

        System.out.println("Init " + network1.getServiceId() + ", " + network2.getServiceId());

        network2.addPacketHandler((packet, direction, serviceId) -> {
            if (direction == BridgePacketDirection.TO_SERVICE) {
                System.out.println("Received packet: " + packet + " from service " + serviceId);
            }
        });
        network1.addPacketHandler((packet, direction, serviceId) -> {
            if (direction == BridgePacketDirection.TO_SERVICE) {
                System.out.println("Received packet: " + packet + " from service " + serviceId);
            }
        });

        TestPacket packet = new TestPacket();
        packet.setTexts(new String[]{"Test", "123"});
        network1.sendPacket(packet);
    }
}
