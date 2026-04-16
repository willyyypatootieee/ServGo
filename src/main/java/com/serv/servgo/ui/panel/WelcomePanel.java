package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.HierarchyEvent;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class WelcomePanel extends JPanel implements ScreenView {
    private static final List<LiveQueueItem> LIVE_QUEUE = List.of(
            new LiveQueueItem("A-101", "B 1523 EV", "Andi Saputra"),
            new LiveQueueItem("A-102", "L 8891 OK", "Budi Pranata"),
            new LiveQueueItem("A-103", "N 3007 EV", "Citra Lestari"),
            new LiveQueueItem("A-104", "W 4408 EV", "Dewi Kartika"),
            new LiveQueueItem("A-105", "AG 7710 OK", "Eko Wibowo"),
            new LiveQueueItem("A-106", "S 2219 EV", "Fajar Nugroho")
    );

    private final KioskController controller;
    private final JLabel queueNoLabel = new JLabel("A-101", SwingConstants.CENTER);
    private final JLabel plateLabel = new JLabel("Plat: -", SwingConstants.CENTER);
    private final JLabel nameLabel = new JLabel("Nama: -", SwingConstants.CENTER);
    private final JLabel announceLabel = new JLabel("PANGGILAN ANTRIAN LIVE", SwingConstants.CENTER);
    private final Timer rotateTimer;

    private int queueIndex;

    public WelcomePanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(20, 20));
        setBorder(UiKit.screenPadding());

        JLabel title = UiKit.title("ServGo!");
        JLabel subtitle = UiKit.big("Pelayanan Cepat. Minim Chaos.");
        JLabel note = UiKit.normal("Tekan Mulai");

        JPanel center = new JPanel(new BorderLayout(14, 14));

        JPanel intro = new JPanel(new GridLayout(3, 1, 10, 10));
        intro.setOpaque(false);
        intro.add(title);
        intro.add(subtitle);
        intro.add(note);

        JPanel liveBoard = buildLiveBoard();

        center.add(intro, BorderLayout.NORTH);
        center.add(liveBoard, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
        add(UiKit.bigButton("Tekan Untuk Mulai", controller::startSession), BorderLayout.SOUTH);

        rotateTimer = new Timer(nextDelayMs(), e -> {
            advanceQueue();
            Timer timer = (Timer) e.getSource();
            timer.setInitialDelay(nextDelayMs());
            timer.restart();
        });
        rotateTimer.setRepeats(false);

        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && !isShowing()) {
                rotateTimer.stop();
            }
        });

        applyQueueItem(LIVE_QUEUE.get(0));
    }

    @Override
    public ScreenId id() {
        return ScreenId.WELCOME;
    }

    @Override
    public JComponent component() {
        return this;
    }

    @Override
    public void onShow() {
        queueIndex = 0;
        applyQueueItem(LIVE_QUEUE.get(queueIndex));
        rotateTimer.setInitialDelay(nextDelayMs());
        rotateTimer.restart();
    }

    private JPanel buildLiveBoard() {
        JPanel liveBoard = new JPanel(new BorderLayout(8, 8));
        liveBoard.setBorder(new EmptyBorder(16, 16, 16, 16));
        liveBoard.putClientProperty("FlatLaf.style", "arc:18;background:lighten(@background,2%)");

        announceLabel.setOpaque(true);
        announceLabel.setBackground(new Color(0x1E88E5));
        announceLabel.setForeground(Color.WHITE);
        announceLabel.setFont(UiKit.normalFont());
        announceLabel.setBorder(new EmptyBorder(10, 12, 10, 12));

        queueNoLabel.setFont(UiKit.titleFont().deriveFont(76f));
        plateLabel.setFont(UiKit.bigFont());
        nameLabel.setFont(UiKit.normalFont());

        JPanel queueInfo = new JPanel(new GridLayout(3, 1, 6, 6));
        queueInfo.setOpaque(false);
        queueInfo.add(queueNoLabel);
        queueInfo.add(plateLabel);
        queueInfo.add(nameLabel);

        liveBoard.add(announceLabel, BorderLayout.NORTH);
        liveBoard.add(queueInfo, BorderLayout.CENTER);
        return liveBoard;
    }

    private void advanceQueue() {
        queueIndex = (queueIndex + 1) % LIVE_QUEUE.size();
        applyQueueItem(LIVE_QUEUE.get(queueIndex));
    }

    private void applyQueueItem(LiveQueueItem item) {
        queueNoLabel.setText(item.queueNo());
        plateLabel.setText("Plat: " + item.plateNo());
        nameLabel.setText("Nama: " + item.customerName());
    }

    private int nextDelayMs() {
        return ThreadLocalRandom.current().nextInt(5_000, 10_001);
    }

    private record LiveQueueItem(String queueNo, String plateNo, String customerName) {
    }
}
