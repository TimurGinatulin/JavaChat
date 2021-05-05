package Lesson_7.source.client.gui;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

public class InputFieldKeyListener implements KeyListener {
    private final JTextField inputField;
    private final JTextArea top;
    private final StringBuilder ss = new StringBuilder();
    private final Consumer<String> consumer;

    public InputFieldKeyListener(JTextField inputField, JTextArea jTextArea, Consumer<String> consumer) {
        this.inputField = inputField;
        this.top = jTextArea;
        this.consumer = consumer;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (inputField.getText().isBlank()) {
                return;
            }
            String newMessage = inputField.getText();
            JTextArea textArea = top;
            ss.append(textArea.getText())
                    .append(newMessage)
                    .append("\n");
            textArea.setText(ss.toString());
            consumer.accept(newMessage);
            inputField.setText("");
            ss.setLength(0);
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}
