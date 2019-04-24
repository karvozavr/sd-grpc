package hse.banana.chat;

import java.util.function.Consumer;

public abstract class AbstractMessenger {
    abstract void sendMessage(String login, String message);

    abstract void subcribe(Consumer<String> messageAccepter);
}
