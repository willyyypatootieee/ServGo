package com.serv.servgo.ui;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

public final class UiKit {
    private static final Border SCREEN_PADDING = BorderFactory.createEmptyBorder(18, 18, 18, 18);

    private UiKit() {
    }

    public static void applyFlatDefaults() {
        UIManager.put("Component.arc", 14);
        UIManager.put("Button.arc", 16);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Component.innerFocusWidth", 0);
        UIManager.put("Button.minimumHeight", 44);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.width", 12);
    }

    public static Border screenPadding() {
        return SCREEN_PADDING;
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(titleFont());
        return label;
    }

    public static JLabel big(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(bigFont());
        return label;
    }

    public static JLabel normal(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(normalFont());
        return label;
    }

    public static Font titleFont() {
        return baseLabelFont().deriveFont(Font.BOLD, 48f);
    }

    public static Font bigFont() {
        return baseLabelFont().deriveFont(Font.BOLD, 32f);
    }

    public static Font normalFont() {
        return baseLabelFont().deriveFont(Font.PLAIN, 24f);
    }

    public static JButton bigButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(baseLabelFont().deriveFont(Font.BOLD, 30f));
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.putClientProperty("FlatLaf.style", "arc:16;focusWidth:1;minimumHeight:48;innerFocusWidth:0");
        button.addActionListener(e -> action.run());
        return button;
    }

    public static JTextArea readOnlyArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        return area;
    }

    private static Font baseLabelFont() {
        Font font = UIManager.getFont("Label.font");
        return font != null ? font : new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    }
}