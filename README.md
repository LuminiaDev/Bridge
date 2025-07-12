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
        .serviceId("my-service-id") // You can remove this parameter and the id will be generated automatically
        .build());

// Creating a codec with an example packet
// If any packet is not found in the codec, it will be handled as BridgeUnknownPacket.
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

## Maven
Adding repo:
```xml
<repositories>
    <repository>
        <id>luminiadev-repository-snapshots</id>
        <url>https://repo.luminiadev.com/snapshots</url>
    </repository>
</repositories>
```

For adding a library only:
```xml
<dependency>
    <groupId>com.luminiadev.bridge</groupId>
    <artifactId>bridge-common</artifactId>
    <version>1.0.6-SNAPSHOT</version>
</dependency>
```

For adding a library RabbitMQ implementation:
```xml
<dependency>
    <groupId>com.luminiadev.bridge</groupId>
    <artifactId>bridge-rabbitmq</artifactId>
    <version>1.0.6-SNAPSHOT</version>
</dependency>
```
## Gradle
Adding repo:
```groovy
maven {
    name = "luminiadevRepositorySnapshots"
    url = uri("https://repo.luminiadev.com/snapshots")
}
```

For adding a library only:
```groovy
implementation "com.luminiadev.bridge:bridge-common:1.0.6-SNAPSHOT"
```

For adding a library RabbitMQ implementation:
```groovy
implementation "com.luminiadev.bridge:bridge-rabbitmq:1.0.6-SNAPSHOT"
```