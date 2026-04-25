package com.serv.servgo.ui.component;

import com.serv.servgo.ui.UiKit;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VirtualKeyboardPanel extends JPanel {
    private final JTextField target;

    public VirtualKeyboardPanel(JTextField target) {
        this.target = target;
        setLayout(new GridLayout(5, 8, 6, 6));

        for (char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
            addKey(String.valueOf(c), () -> append(String.valueOf(c)));
        }

        for (char c : "0123456789".toCharArray()) {
            addKey(String.valueOf(c), () -> append(String.valueOf(c)));
        }

        addKey("⌫", this::backspace);
        addKey("Clear", () -> target.setText(""));
        addKey("Space", () -> append(" "));
        addKey("Demo Data", () -> target.setText("L1234EV"));
    }

    private void addKey(String label, Runnable action) {
        JButton button = UiKit.bigButton(label, action);
        add(button);
    }

    private void append(String value) {
        target.setText(target.getText() + value);
    }

    private void backspace() {
        String text = target.getText();
        if (!text.isEmpty()) {
            target.setText(text.substring(0, text.length() - 1));
        }
    }
}