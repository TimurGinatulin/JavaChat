package Lesson_7.source.server.auth;

import Lesson_7.source.server.Server;
import Lesson_7.source.server.database.DBHandler;
import Lesson_7.source.server.database.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final Server server;
    private int id;
    private String name;
    private final Socket client;
    private final DataOutputStream out;
    private final DataInputStream in;
    private final DBHandler dbHandler;
    private static final Logger log = LogManager.getLogger(ClientHandler.class.getName());

    public ClientHandler(Socket client, Server server) {
        PropertyConfigurator.configure("log4j.properties");
        dbHandler = new DBHandler();
        this.server = server;
        this.client = client;
        try {
            this.out = new DataOutputStream(client.getOutputStream());
            this.in = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            log.error("IO exception");
            throw new RuntimeException("SWW", e);
        }
        listen();
    }

    private void listen() {
        try {
            sendMessage("""
                    For login use: -auth "Login" "Password"
                    For unicast receive message use: /w "nick" "Message"
                    For logout use /exit
                    For change nickname use: /c "new_nick\"""");
            //client.setSoTimeout(15_000);
            doAuth();
            readMessage();
        } catch (IOException exception) {
            System.out.println("Error in connection with " + client);
            log.error("Error in connection with " + client);
            server.unSubscribe(this);
            server.printAtSerer(client + " shutdown");
        }
    }

    public void sendMessage(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException exception) {
            exception.printStackTrace();
            log.error("Error in send message");
            System.out.println("Error in send message");
        }
    }

    public String getName() {
        return name;
    }

    private void doAuth() throws IOException {
        User user;
        while (true) {
            String input = in.readUTF();
            if (input.startsWith("-auth")) {
                String[] credentials = input.split("\\s");
                user = dbHandler.authentication(credentials[1], credentials[2]);
                if (user != null) {
                    if (server.isNickFree(credentials[1])) {
                        sendMessage("CMD: auth OK!");
                        name = user.getName();
                        id = user.getId();
                        server.broadcast(name + " login");
                        server.subscribe(this);
                        server.printAtSerer(name + " connected. At " + client);
                        log.info(name + " connected. At " + client);
                        return;
                    } else {
                        sendMessage("Current user already login-up");
                        server.printAtSerer("Repeated entry at " + credentials[1] + " at "
                                + client);
                    }
                } else {
                    sendMessage("Unknown user");
                }
            } else {
                sendMessage("Invalid authentication request.");
            }
        }
    }

    public void readMessage() throws IOException {
        while (client.isBound()) {
            String message = in.readUTF();
            String[] messageSplit = message.split("\\s");
            switch (messageSplit[0]) {
                case "/w" -> {
                    String nick = messageSplit[1];
                    server.unicast(name, nick, getMessage(messageSplit, false));
                    log.info("User " + getName() + " send " + nick + " "
                            + getMessage(messageSplit, false));
                }
                case "/exit" -> {
                    server.unSubscribe(this);
                    sendMessage("qwerty");
                    server.printAtSerer("Client " + name + " logout");
                    log.info("User " + getName() + " logout.");
                    client.close();
                    in.close();
                    out.close();
                }
                case "/c" -> {
                    server.unSubscribe(this);
                    server.broadcast("user " + name + " change nick-->" + messageSplit[1]);
                    name = messageSplit[1];
                    dbHandler.update(new User(id, messageSplit[1]));
                    server.subscribe(this);
                    log.info("user " + name + " change nick-->" + messageSplit[1]);
                }
                default -> {
                    server.broadcast(getMessage(messageSplit, true));
                    log.info("user " + name + " send " + getMessage(messageSplit, true));
                }
            }
        }
    }

    private String getMessage(String[] message, boolean isBroadcast) {
        int start;
        if (isBroadcast) start = 0;
        else start = 2;
        StringBuilder msg = new StringBuilder(name + ": ");
        for (int i = start; i < message.length; i++) {
            msg.append(message[i]).append(" ");
        }
        return msg.toString();
    }

}
