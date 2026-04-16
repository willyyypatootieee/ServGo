package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import com.serv.servgo.ui.component.VirtualKeyboardPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CheckInPanel extends JPanel implements ScreenView {
    private final KioskController controller;
    private final JTextField plateField = new JTextField();
    private final JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);

    public CheckInPanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(UiKit.title("Mobil Masuk"), BorderLayout.NORTH);

        plateField.setFont(new Font("Monospaced", Font.BOLD, 32));
        plateField.setHorizontalAlignment(SwingConstants.CENTER);
        plateField.setEditable(false);
        add(plateField, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(8, 8));
        south.add(new VirtualKeyboardPanel(plateField), BorderLayout.CENTER);

        messageLabel.setForeground(Color.RED);
        south.add(messageLabel, BorderLayout.NORTH);

        JPanel actions = new JPanel(new BorderLayout(8, 8));
        actions.add(UiKit.bigButton("Kembali", controller::goHome), BorderLayout.WEST);
        actions.add(UiKit.bigButton("Lanjut", () -> {
            boolean ok = controller.submitPlate(plateField.getText());
            if (!ok) {
                messageLabel.setText("Plat Nomor Harus 4 Karakter!");
            }
        }), BorderLayout.EAST);

        south.add(actions, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
    }

    @Override
    public ScreenId id() {
        return ScreenId.CHECK_IN;
    }

    @Override
    public JComponent component() {
        return this;
    }

    @Override
    public void onShow() {
        plateField.setText("");
        messageLabel.setText(" ");
    }
}
