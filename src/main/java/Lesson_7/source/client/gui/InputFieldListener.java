package Lesson_7.source.client.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class InputFieldListener implements ActionListener {
    private final JTextField inputField;
    private final JTextArea top;
    private final StringBuilder ss = new StringBuilder();
    private final Consumer<String> consumer;

    public InputFieldListener(JTextField inputField, JTextArea top, Consumer<String> consumer) {
        this.inputField = inputField;
        this.top = top;
        this.consumer = consumer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
