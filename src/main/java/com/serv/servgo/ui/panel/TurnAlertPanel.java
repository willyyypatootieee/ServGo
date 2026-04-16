package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TurnAlertPanel extends JPanel implements ScreenView {
    private final KioskController controller;

    public TurnAlertPanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(UiKit.screenPadding());

        JLabel alert = new JLabel("YOUR TURN NOW", JLabel.CENTER);
        alert.setFont(UiKit.titleFont());
        add(alert, BorderLayout.CENTER);

        JLabel note = UiKit.big("Beep sent. Proceed to service now.");
        add(note, BorderLayout.NORTH);

        add(UiKit.bigButton("Start Process", controller::startProcess), BorderLayout.SOUTH);
    }

    @Override
    public ScreenId id() {
        return ScreenId.ALERT;
    }

    @Override
    public JComponent component() {
        return this;
    }

    @Override
    public void onShow() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }
}
