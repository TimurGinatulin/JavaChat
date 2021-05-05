package Lesson_7.source.client;

import Lesson_7.source.client.network.ClientNetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BasicChatNetwork implements ClientNetwork {
    private Socket client;
    private DataOutputStream out;
    private DataInputStream in;
    static boolean isConnect;

    public BasicChatNetwork(String host, int port) {
        new Thread(() -> {
            try {
                client = new Socket(host, port);
                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());
                isConnect = true;
            } catch (IOException e) {
                System.out.println("connection lost");
                throw new RuntimeException("SWW", e);
            }
        }).start();
    }

    @Override
    public void send(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException("SWW during sending", e);
        }
    }

    @Override
    public String receive() {
        while (true) {
            try {
                String input = in.readUTF();
                if (input.equals("qwerty")) {
                    System.exit(1);
                }
                return input;
            } catch (IOException e) {
                throw new RuntimeException("SWW during receive", e);
            }
        }
    }
}