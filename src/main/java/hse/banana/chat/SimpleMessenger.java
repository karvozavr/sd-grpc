package hse.banana.chat;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

public class SimpleMessenger extends AbstractMessenger {
    Queue<String> kek = new ArrayDeque<>();
    private Consumer<String> messageAccepter;


    @Override
    void sendMessage(String login, String message) {
        kek.add(login + ": " + message);
        if (messageAccepter != null) {
            while (!kek.isEmpty()) messageAccepter.accept(kek.poll());
        }
    }

    @Override
    void subcribe(Consumer<String> messageAccepter) {
        this.messageAccepter = messageAccepter;
    }
}
