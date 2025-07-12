# LumiBridge
A library for communicating by sending packets between multiple services, based on RabbitMQ

## Introduction
What is it for? This library helps with communication between multiple services connected to the same RabbitMQ by sending and processing custom packets.

## Example of usage
```java
import com.luminiadev.bridge.network.BridgeRabbitMQConfig;
import com.luminiadev.bridge.network.BridgeRabbitMQCredentials;
import com.luminiadev.bridge.network.BridgeRabbitMQNetwork;
import com.luminiadev.bridge.network.codec.BridgeCodec;
import com.luminiadev.bridge.network.codec.packet.BridgePacketDirection;

// Creating a LumiBridge implementation instance on RabbitMQ
BridgeRabbitMQNetwork network = new BridgeRabbitMQNetwork(BridgeRabbitMQConfig.builder()
        .host("localhost")
        .credentials(new BridgeRabbitMQCredentials("guest", "guest"))
        .build());

// Creating a codec with an example packet
BridgeCodec codec = BridgeCodec.builder()
        .registerPacket("example_packet", ExamplePacket::new, new ExamplePacketSerializer())
        .build();

network.setCodec(codec); // Set codec to network
network.start(); // Start network

// Adding packet handler
network.addPacketHandler((packet, direction, serviceId) -> {
    if (direction == BridgePacketDirection.TO_SERVICE) {
        System.out.println("Received packet " + packet + " from service " + serviceId);
    } else {
        System.out.println("Sent packet: " + packet);
    }
});
```