package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MapsPanel extends JPanel implements ScreenView {
    private final KioskController controller;
    private final JTextArea mapArea = new JTextArea();

    public MapsPanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(UiKit.title("Rest Area Map"), BorderLayout.NORTH);

        mapArea.setEditable(false);
        mapArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        mapArea.setText("""
                [Tempat Charge]  [Service Bay]
                [Tenant]        [Prayer Room]
                [Toilet]            [ATM / Info]
                """);
        add(new JScrollPane(mapArea), BorderLayout.CENTER);

        add(UiKit.bigButton("Back", controller::backToServiceSelection), BorderLayout.SOUTH);
    }

    @Override
    public ScreenId id() {
        return ScreenId.MAPS;
    }

    @Override
    public JComponent component() {
        return this;
    }
}
