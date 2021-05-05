package Lesson_7.source.client.gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ChatFrame extends JFrame {
    private final JPanel top;
    private JTextArea chatArea;

    public ChatFrame(String title, Consumer<String> messageConsumer) throws HeadlessException {
        setTitle(title);
        setBounds(0, 0, 500, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        top = createTop();
        JPanel bottom = createBottom(messageConsumer);
        add(top, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setVisible(true);
    }


    public JTextArea getChatArea() {
        return chatArea;
    }

    private JPanel createBottom(Consumer<String> messageConsumer) {
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        JTextField inputField = new JTextField();
        inputField.addKeyListener(new InputFieldKeyListener(inputField,chatArea , messageConsumer));
        Button submit = new Button("SUBMIT");
        submit.addActionListener(new InputFieldListener(inputField, chatArea, messageConsumer));
        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(submit, BorderLayout.EAST);
        return bottom;
    }

    private JPanel createTop() {
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(chatArea);
        scroll.setWheelScrollingEnabled(true);
        scroll.setAutoscrolls(true);
        JTextArea activeUser = new JTextArea();
        chatArea.setEditable(false);

        top.add(scroll, BorderLayout.CENTER);
        top.add(activeUser, BorderLayout.WEST);
        return top;
    }
}
