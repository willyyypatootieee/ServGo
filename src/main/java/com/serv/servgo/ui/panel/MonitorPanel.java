package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class MonitorPanel extends JPanel implements ScreenView {
    private final KioskController controller;
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel percentLabel = new JLabel("0%", JLabel.CENTER);
    private final JLabel etaLabel = new JLabel("ETA: --", JLabel.CENTER);

    public MonitorPanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(UiKit.screenPadding());

        add(UiKit.title("Real-Time Proses Monitor"), BorderLayout.NORTH);

        progressBar.setStringPainted(true);
        progressBar.putClientProperty("FlatLaf.style", "arc:12");
        add(progressBar, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new java.awt.GridLayout(2, 1, 8, 8));
        percentLabel.setFont(UiKit.bigFont());
        etaLabel.setFont(UiKit.normalFont());
        bottom.add(percentLabel);
        bottom.add(etaLabel);
        add(bottom, BorderLayout.SOUTH);
    }

    @Override
    public ScreenId id() {
        return ScreenId.MONITOR;
    }

    @Override
    public JComponent component() {
        return this;
    }

    @Override
    public void onShow() {
        setProgress(0, Math.max(0, controller.getSession().getEstimatedWaitMinutes()));
    }

    public void setProgress(int percent, int etaMinutes) {
        progressBar.setValue(percent);
        progressBar.setString(percent + "%");
        percentLabel.setText(percent + "% complete");
        etaLabel.setText("Estimated completion: " + etaMinutes + " min");
    }
}
