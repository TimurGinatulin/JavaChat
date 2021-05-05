package Lesson_7.source.server;

import Lesson_7.source.server.auth.ClientHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    private final ServerSocket server;
    private final Set<ClientHandler> handlers;
    private static final Logger logger = LogManager.getLogger(Server.class.getName());


    public Server(int port) {
        PropertyConfigurator.configure("log4j.properties");
        handlers = new HashSet<>();
        try {
            this.server = new ServerSocket(port);
            logger.info("Server run");
            printAtSerer("Server run...");
            init();
        } catch (IOException e) {
            printAtSerer("Server down.");
            logger.error("Server down");
            throw new RuntimeException("SWW", e);
        }
    }

    private void init() throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        while (!server.isClosed()) {
            printAtSerer("Server waiting connection...");
            Socket client = server.accept();
            System.out.println("Client " + client + " accept");
            logger.info("Client " + client + " accept");
            executorService.execute(() -> new ClientHandler(client, this));

        }
        executorService.shutdown();
    }

    public synchronized boolean isNickFree(String name) {
        for (ClientHandler handler : handlers) {
            if (handler.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public synchronized void broadcast(String msg) {
        for (ClientHandler handler : handlers) {
            handler.sendMessage(msg);
        }

    }

    public void unicast(String name, String nick, String msg) {
        for (ClientHandler handler : handlers) {
            if (handler.getName().equals(nick) || handler.getName().equals(name))
                handler.sendMessage(msg);
        }
    }

    public synchronized void subscribe(ClientHandler handler) {
        handlers.add(handler);
    }

    public synchronized void unSubscribe(ClientHandler handler) {
        handlers.remove(handler);
    }

    public void printAtSerer(String message) {
        System.out.println(message);
    }
}
