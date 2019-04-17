package hse.banana.chat;

import hse.banana.chat.proto.BananaChatGrpc;
import hse.banana.chat.proto.Chat;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;

public class ChatService extends BananaChatGrpc.BananaChatImplBase {

    private ArrayList<String> messages;

    @Override
    public void sendMessage(Chat.Message request, StreamObserver<Chat.Empty> responseObserver) {
        String message = request.getText();
        messages.add(message);
        responseObserver.onCompleted();
    }
}
