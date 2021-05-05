package Lesson_7.source.client.history;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HistoryHandler {
    public HistoryHandler() {
        File file = new File("src/Lesson_7/source/client/history/history.txt");
        if (!file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void write(String message) {
        try (FileWriter fileWriter =
                     new FileWriter("src/Lesson_7/source/client/history/history.txt", true)) {
            fileWriter.write(message);
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println("Error which write message.");
        }
    }

    public String read(int sizeBuf) {
        StringBuilder ss = new StringBuilder();
        int count = 0;
        try (FileInputStream fileReader =
                     new FileInputStream("src/Lesson_7/source/client/history/history.txt");
             Scanner scanner = new Scanner(fileReader)) {
            while (count < sizeBuf && scanner.hasNextLine()) {
                ss.append(scanner.nextLine());
                ss.append("\n");
                count++;
            }

        } catch (IOException e) {
            System.out.println("Error in read History");
        }
        return ss.toString();
    }

}

