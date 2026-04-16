package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QueuePanel extends JPanel implements ScreenView {
    private final KioskController controller;
    private final JLabel queueLabel = new JLabel("", JLabel.CENTER);
    private final JLabel waitLabel = new JLabel("", JLabel.CENTER);
    private final JLabel statusLabel = new JLabel("", JLabel.CENTER);

    public QueuePanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(UiKit.title("Sistem Antri"), BorderLayout.NORTH);

        JPanel center = new JPanel(new java.awt.GridLayout(3, 1, 8, 8));
        queueLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        waitLabel.setFont(new Font("SansSerif", Font.PLAIN, 26));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        center.add(queueLabel);
        center.add(waitLabel);
        center.add(statusLabel);
        add(center, BorderLayout.CENTER);

        JLabel tip = UiKit.normal("Fasilitas Terdekat: Food Cour, Toilet, Mushola");
        add(tip, BorderLayout.SOUTH);

        JPanel actions = new JPanel(new java.awt.GridLayout(1, 2, 12, 12));
        actions.add(UiKit.bigButton("Beri Tahu Giliran Saya!", controller::notifyTurn));
        actions.add(UiKit.bigButton("Ganti Pelayanan", controller::backToServiceSelection));
        add(actions, BorderLayout.EAST);
    }

    @Override
    public ScreenId id() {
        return ScreenId.QUEUE;
    }

    @Override
    public JComponent component() {
        return this;
    }

    @Override
    public void onShow() {
        queueLabel.setText("Waktu Tunggu: " + controller.getSession().getQueueNumber());
        waitLabel.setText("Estimasi Waktu Tunggu: " + controller.getSession().getEstimatedWaitMinutes() + " minutes");
        statusLabel.setText("Status: Menunggu");
    }
}
