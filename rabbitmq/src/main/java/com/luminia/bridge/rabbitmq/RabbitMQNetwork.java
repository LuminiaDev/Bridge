package com.luminia.bridge.rabbitmq;

import com.luminia.bridge.network.AbstractBridgeNetwork;
import com.luminia.bridge.network.BridgeNetwork;
import com.luminia.bridge.network.handler.BridgePacketHandler;
import com.luminia.bridge.network.packet.BridgePacket;
import com.luminia.bridge.network.packet.BridgePacketDefinition;
import com.luminia.bridge.network.packet.BridgePacketDirection;
import com.luminia.bridge.network.packet.serializer.BridgePacketSerializer;
import com.luminia.bridge.network.packet.serializer.BridgePacketSerializerHelper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.DefaultCredentialsProvider;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQNetwork extends AbstractBridgeNetwork {

    public static final String PACKET_SEND_EXCHANGE = "lumibridge.packet_send_exchange";

    private final Connection connection;
    private final Channel channel;

    private String queueName;

    public RabbitMQNetwork(String host, String username, String password) {
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
            channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
                ByteBuf buffer = Unpooled.wrappedBuffer(delivery.getBody());
                BridgePacketSerializerHelper helper = new BridgePacketSerializerHelper();
                String packetId = helper.readString(buffer);
                BridgePacket packet = this.tryDecode(buffer, helper, packetId);
                for (BridgePacketHandler packetHandler : this.getPacketHandlers()) {
                    packetHandler.handle(packet, BridgePacketDirection.TO_SERVICE);
                }
            }, consumerTag -> {});
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
        ByteBuf buffer = Unpooled.buffer();
        BridgePacketSerializerHelper helper = new BridgePacketSerializerHelper();
        helper.writeString(buffer, packet.getId());
        this.tryEncode(buffer, helper, packet);
        for (BridgePacketHandler packetHandler : this.getPacketHandlers()) {
            packetHandler.handle(packet, BridgePacketDirection.FROM_SERVICE);
        }
        try {
            channel.basicPublish(PACKET_SEND_EXCHANGE, "", null, buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
