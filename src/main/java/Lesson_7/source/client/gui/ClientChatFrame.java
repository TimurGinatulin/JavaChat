package Lesson_7.source.client.gui;

import java.util.function.Consumer;

public class ClientChatFrame implements ChatFrameInteraction {
    private final ChatFrame chatFrame;
    private final Consumer<String> messageConsumer;

    public ClientChatFrame(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;
        this.chatFrame = new ChatFrame("Client chat v1.0", messageConsumer);
    }

    @Override
    public void append(String message) {
        chatFrame.getChatArea().append(message);
        chatFrame.getChatArea().append("\n");
    }
}
