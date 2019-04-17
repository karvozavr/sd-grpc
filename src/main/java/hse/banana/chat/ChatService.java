package hse.banana.chat;

import hse.banana.chat.proto.BananaChatGrpc;
import hse.banana.chat.proto.Chat;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;

public class ChatService extends BananaChatGrpc.BananaChatImplBase {

    private ArrayList<Chat.Message> messages;

    @Override
    public void sendMessage(Chat.SendMessageRequest request, StreamObserver<Chat.Empty> responseObserver) {
        Chat.Message message = request.getMessage();
        messages.add(message);
        responseObserver.onCompleted();
    }
}
