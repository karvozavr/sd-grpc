package hse.banana.chat;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@SuppressWarnings("Duplicates")
public class RabbitMessenger extends AbstractMessenger {

    private final String channelName;
    private final String host;

    public RabbitMessenger(String channelName, String host) {
        this.channelName = channelName;
        this.host = host;
    }

    @Override
    void sendMessage(String login, String message) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(channelName, "fanout");

            channel.basicPublish(channelName, "", null, (login + ": " + message).getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    void subcribe(Consumer<String> messageAccepter) {
        try {
            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost(host);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(channelName, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, channelName, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
                messageAccepter.accept(message);
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
