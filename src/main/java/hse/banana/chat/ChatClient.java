package hse.banana.chat;

import hse.banana.chat.proto.BananaChatGrpc;
import hse.banana.chat.proto.Chat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient {
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());


    private ManagedChannel channel;
    private BananaChatGrpc.BananaChatBlockingStub blockingStub;

    public ChatClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build());
    }

    private ChatClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = BananaChatGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void sendMessage(String message) {
        logger.info("Sending message: " + message);


        Chat.Message request = Chat.Message.newBuilder().setText(message).build();
        try {
            blockingStub.sendMessage(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
        }
    }

    public List<String> getMessages() {
        logger.info("Retrieving messages");

        Chat.User request = Chat.User.newBuilder().setName("Jakuza").build();
        Iterator<Chat.Message> response;
        try {
            response = blockingStub.getMessages(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return Collections.emptyList();
        }

        ArrayList<String> list = new ArrayList<>();
        response.forEachRemaining((msg) -> list.add(msg.getText()));
        return list;
    }
}
