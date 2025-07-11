package com.luminia.bridge.network;

import com.luminia.bridge.exception.BridgeRabbitMQException;
import com.luminia.bridge.network.codec.packet.BridgePacket;
import com.luminia.bridge.network.codec.packet.BridgePacketDirection;
import com.luminia.bridge.network.codec.packet.handler.BridgePacketHandler;
import com.luminia.bridge.util.ByteBuffer;
import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.DefaultCredentialsProvider;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class BridgeRabbitMQNetwork extends AbstractBridgeNetwork {

    public static final String PACKET_SEND_EXCHANGE = "lumibridge.packet_send_exchange";

    private final String serviceId;

    private final Connection connection;
    private final Channel channel;

    private String queueName;

    public BridgeRabbitMQNetwork(BridgeRabbitMQConfig config) {
        this.serviceId = UUID.randomUUID().toString();

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(config.getHost());
        connectionFactory.setPort(config.getPort());
        connectionFactory.setVirtualHost(config.getVirtualHost());
        connectionFactory.setCredentialsProvider(new DefaultCredentialsProvider(
                config.getCredentials().username(),
                config.getCredentials().password()
        ));
        try {
            this.connection = connectionFactory.newConnection();
            this.channel = connection.createChannel();
            this.channel.exchangeDeclare(PACKET_SEND_EXCHANGE, BuiltinExchangeType.FANOUT);
        } catch (IOException | TimeoutException e) {
            throw new BridgeRabbitMQException("Failed to create channel and declare exchange", e);
        }
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public void start() {
        try {
            this.queueName = channel.queueDeclare().getQueue();
            this.channel.queueBind(queueName, PACKET_SEND_EXCHANGE, "");
            this.channel.basicConsume(queueName, true, this::handleDelivery, consumerTag -> {});
        } catch (IOException e) {
            throw new BridgeRabbitMQException("Failed to start RabbitMQ network", e);
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
            throw new BridgeRabbitMQException("Failed to close RabbitMQ connection", e);
        }
    }

    @Override
    public <T extends BridgePacket> void sendPacket(T packet) {
        ByteBuffer buffer = ByteBuffer.of(Unpooled.buffer());
        buffer.writeString(packet.getId());
        buffer.writeString(this.serviceId);
        this.tryEncode(buffer, packet);

        for (BridgePacketHandler packetHandler : this.getPacketHandlers()) {
            packetHandler.handle(packet, BridgePacketDirection.FROM_SERVICE, this.serviceId);
        }

        try {
            this.channel.basicPublish(PACKET_SEND_EXCHANGE, "", null, buffer.array());
        } catch (IOException e) {
            throw new BridgeRabbitMQException("Failed to send packet: " + packet.getId(), e);
        }
    }

    protected void handleDelivery(String consumerTag, Delivery delivery) {
        try {
            ByteBuffer buffer = ByteBuffer.of(Unpooled.wrappedBuffer(delivery.getBody()));
            String packetId = buffer.readString();
            String senderId = buffer.readString();

            if (senderId.equals(this.serviceId)) {
                return;
            }

            BridgePacket packet = this.tryDecode(buffer, packetId);

            for (BridgePacketHandler packetHandler : this.getPacketHandlers()) {
                packetHandler.handle(packet, BridgePacketDirection.TO_SERVICE, senderId);
            }
        } catch (Exception e) {
            throw new BridgeRabbitMQException("Failed to handle delivery: ", e);
        }
    }
}
