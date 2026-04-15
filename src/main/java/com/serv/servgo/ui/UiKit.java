package com.serv.servgo.ui;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public final class UiKit {
    private UiKit() {
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 40));
        return label;
    }

    public static JLabel big(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 28));
        return label;
    }

    public static JLabel normal(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 20));
        return label;
    }

    public static JButton bigButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 22));
        button.addActionListener(e -> action.run());
        return button;
    }

    public static JTextArea readOnlyArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Monospaced", Font.PLAIN, 18));
        return area;
    }
}