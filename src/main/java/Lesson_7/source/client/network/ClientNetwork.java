package Lesson_7.source.client.network;

public interface ClientNetwork {
    void send(String message);

    String receive();
}
