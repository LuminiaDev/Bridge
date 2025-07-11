package com.luminia.bridge.network;

import com.luminia.bridge.network.AbstractBridgeNetwork;
import com.luminia.bridge.network.packet.BridgePacket;
import com.luminia.bridge.network.packet.BridgePacketDirection;
import com.luminia.bridge.network.packet.handler.BridgePacketHandler;
import com.luminia.bridge.util.ByteBuffer;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.DefaultCredentialsProvider;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BridgeRabbitMQNetwork extends AbstractBridgeNetwork {

    public static final String PACKET_SEND_EXCHANGE = "lumibridge.packet_send_exchange";

    private final Connection connection;
    private final Channel channel;

    private String queueName;

    public BridgeRabbitMQNetwork(String host, String username, String password) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setCredentialsProvider(new DefaultCredentialsProvider(username, password));
        try {
            this.connection = connectionFactory.newConnection();
            this.channel = connection.createChannel();
            this.channel.exchangeDeclare(PACKET_SEND_EXCHANGE, BuiltinExchangeType.FANOUT);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        try {
            this.queueName = channel.queueDeclare().getQueue();
            this.channel.queueBind(queueName, PACKET_SEND_EXCHANGE, "");
            this.channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
                ByteBuffer buffer = ByteBuffer.of(Unpooled.wrappedBuffer(delivery.getBody()));
                String packetId = buffer.readString();
                BridgePacket packet = this.tryDecode(buffer, packetId);
                for (BridgePacketHandler packetHandler : this.getPacketHandlers()) {
                    packetHandler.handle(packet, BridgePacketDirection.TO_SERVICE);
                }
            }, consumerTag -> {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (queueName != null) {
                this.channel.queueUnbind(queueName, PACKET_SEND_EXCHANGE, "");
                this.channel.queueDelete(queueName);
            }
            this.channel.close();
            this.connection.close();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends BridgePacket> void sendPacket(T packet) {
        ByteBuffer buffer = ByteBuffer.of(Unpooled.buffer());
        buffer.writeString(packet.getId());
        this.tryEncode(buffer, packet);
        for (BridgePacketHandler packetHandler : this.getPacketHandlers()) {
            packetHandler.handle(packet, BridgePacketDirection.FROM_SERVICE);
        }
        try {
            this.channel.basicPublish(PACKET_SEND_EXCHANGE, "", null, buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
