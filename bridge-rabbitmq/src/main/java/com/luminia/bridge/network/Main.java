package com.luminia.bridge.network;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.DefaultCredentialsProvider;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public class Main {

    public static final String PACKET_SEND_EXCHANGE = "packet_send_exchange";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setCredentialsProvider(new DefaultCredentialsProvider("guest", "guest"));

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(PACKET_SEND_EXCHANGE, BuiltinExchangeType.FANOUT);

            var queues = new HashSet<String>();
            for (int i = 0; i < 3; i++) {
                var queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, PACKET_SEND_EXCHANGE, "");
                queues.add(queueName);

                System.out.println("Registered consumer");
                channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
                    var message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Received '" + message + "' in queue '" + queueName + "'");
                }, consumerTag -> {});
            }

            for (int i = 0; i < 10; i++) {
                var message = "Hello world #%s".formatted(i);
                channel.basicPublish(PACKET_SEND_EXCHANGE, "", null, message.getBytes(StandardCharsets.UTF_8));
            }

            Thread.sleep(5000);

            for (var queueName: queues) {
                System.out.println(" [x] Deleting queue '" + queueName + "'");
                channel.queueUnbind(queueName, PACKET_SEND_EXCHANGE, "");
                channel.queueDelete(queueName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}