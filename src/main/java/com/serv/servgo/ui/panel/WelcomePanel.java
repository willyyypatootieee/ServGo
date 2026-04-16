package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WelcomePanel extends JPanel implements ScreenView {
    private final KioskController controller;

    public WelcomePanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(20, 20));

        JLabel title = UiKit.title("ServGo!");
        JLabel subtitle = UiKit.big("Pelayanan Cepat. Minim Chaos.");
        JLabel note = UiKit.normal("Tekan Mulai");

        JPanel center = new JPanel(new GridLayout(3, 1, 10, 10));
        center.add(title);
        center.add(subtitle);
        center.add(note);

        add(center, BorderLayout.CENTER);
        add(UiKit.bigButton("Tekan Untuk Mulai", controller::startSession), BorderLayout.SOUTH);
    }

    @Override
    public ScreenId id() {
        return ScreenId.WELCOME;
    }

    @Override
    public JComponent component() {
        return this;
    }
}
