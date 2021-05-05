package Lesson_7.source.client;

import Lesson_7.source.client.gui.ClientChatFrame;
import Lesson_7.source.client.history.HistoryHandler;
import Lesson_7.source.client.network.ClientNetwork;

import java.util.function.Consumer;

public class ClientChatAdapter {
    private final ClientNetwork network;
    private final ClientChatFrame frame;
    private final HistoryHandler historyHandler;

    public ClientChatAdapter(String host, int port) {
        this.historyHandler = new HistoryHandler();
        this.network = new BasicChatNetwork(host, port);
        this.frame = new ClientChatFrame(new Consumer<String>() {
            @Override
            public void accept(String message) {
                network.send(message);
            }
        });
        frame.append(historyHandler.read(100));
        receive();
    }

    private void receive() {
        new Thread(() -> {
            while (true) {
                try {
                    String msg = network.receive();
                    frame.append(msg);
                    historyHandler.write(msg + "\n");
                } catch (RuntimeException e) {
                    System.out.println(e);
                    System.exit(1);
                }
            }
        }).start();
    }
}
