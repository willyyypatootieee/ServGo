package com.serv.servgo.ui;

import com.serv.servgo.model.ScreenId;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class KioskFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);
    private final Map<ScreenId, ScreenView> screens = new EnumMap<>(ScreenId.class);

    public KioskFrame() {
        setTitle("ServGo Rest Area Servis!");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 720));
        setSize(new Dimension(1280, 720));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setContentPane(root);
    }

    public void register(ScreenView screen) {
        screens.put(screen.id(), screen);
        root.add(screen.component(), screen.id().id());
    }

    public void showScreen(ScreenId id) {
        ScreenView screen = screens.get(id);
        if (screen != null) {
            screen.onShow();
            cardLayout.show(root, id.id());
        }
    }
}
