package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.HierarchyEvent;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class QueuePanel extends JPanel implements ScreenView {
    private static final String[] ANNOUNCEMENTS = {
        "Mohon tetap di area tunggu, petugas akan memanggil nomor Anda sebentar lagi.",
        "ServGo menyediakan charging area, service bay, dan fasilitas istirahat.",
        "Jika ingin ganti layanan, gunakan tombol Ganti Pelayanan.",
        "Harap siapkan kendaraan dan pastikan baterai ponsel Anda aman saat menunggu."
    };

    private static final String[] DRIVER_NAMES = {
        "Andi", "Budi", "Citra", "Dewi", "Eko", "Fajar", "Gita", "Hana", "Irfan", "Joko"
    };

    private final KioskController controller;
    private final JLabel queueLabel = new JLabel("", JLabel.CENTER);
    private final JLabel waitLabel = new JLabel("", JLabel.CENTER);
    private final JLabel statusLabel = new JLabel("", JLabel.CENTER);
    private final JLabel announcementLabel = new JLabel("", JLabel.CENTER);
    private final DefaultListModel<QueueTicket> queueModel = new DefaultListModel<>();
    private final JList<QueueTicket> queueList = new JList<>(queueModel);
    private final Timer flashTimer;
    private final Timer announcementTimer;

    private int currentIndex;
    private int announcementIndex;
    private boolean flashOn;

    public QueuePanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(UiKit.screenPadding());

        add(UiKit.title("Sistem Antri"), BorderLayout.NORTH);

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setOpaque(false);

        JPanel summary = new JPanel(new GridLayout(1, 3, 10, 10));
        summary.setOpaque(false);
        queueLabel.setFont(UiKit.titleFont());
        waitLabel.setFont(UiKit.bigFont());
        statusLabel.setFont(UiKit.normalFont());

        summary.add(queueLabel);
        summary.add(waitLabel);
        summary.add(statusLabel);

        announcementLabel.setOpaque(true);
        announcementLabel.setBackground(new Color(0x1E88E5));
        announcementLabel.setForeground(Color.WHITE);
        announcementLabel.setFont(UiKit.normalFont());
        announcementLabel.setBorder(new EmptyBorder(12, 16, 12, 16));

        top.add(summary, BorderLayout.NORTH);
        top.add(announcementLabel, BorderLayout.SOUTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(top, BorderLayout.NORTH);

        queueList.setCellRenderer(new QueueCellRenderer());
        queueList.setFixedCellHeight(90);
        queueList.setVisibleRowCount(6);
        queueList.setSelectionBackground(new Color(0, 0, 0, 0));
        queueList.setSelectionForeground(Color.BLACK);
        queueList.setBackground(new Color(0xFAFAFB));
        queueList.setBorder(new EmptyBorder(8, 8, 8, 8));
        queueList.setFocusable(false);

        JScrollPane queueScroll = new JScrollPane(queueList);
        queueScroll.setBorder(BorderFactory.createLineBorder(new Color(0xE5E7EB), 1, true));
        queueScroll.getViewport().setBackground(Color.WHITE);
        center.add(queueScroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(8, 8));
        JLabel tip = UiKit.normal("Fasilitas terdekat: Food Court, Toilet, Mushola");
        south.add(tip, BorderLayout.NORTH);

        JPanel actions = new JPanel(new java.awt.GridLayout(1, 2, 12, 12));
        actions.add(UiKit.bigButton("Beri Tahu Giliran Saya!", controller::notifyTurn));
        actions.add(UiKit.bigButton("Ganti Pelayanan", controller::backToServiceSelection));
        south.add(actions, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        flashTimer = new Timer(450, e -> {
            flashOn = !flashOn;
            queueList.repaint();
        });
        flashTimer.setRepeats(true);

        announcementTimer = new Timer(2600, e -> rotateAnnouncement());
        announcementTimer.setRepeats(true);

        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (isShowing()) {
                    startTimers();
                } else {
                    stopTimers();
                }
            }
        });
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
        buildDemoQueue();
        updateAnnouncement();
        queueLabel.setText("Nomor Antrian: " + controller.getSession().getQueueNumber());
        waitLabel.setText("Estimasi Tunggu: " + controller.getSession().getEstimatedWaitMinutes() + " menit");
        statusLabel.setText("Status: Menunggu panggilan");
        queueList.ensureIndexIsVisible(currentIndex);
        queueList.repaint();
        startTimers();
    }

    private void buildDemoQueue() {
        queueModel.clear();

        int baseQueue = Math.max(1, controller.getSession().getQueueNumber());
        currentIndex = Math.min(4, 9);
        int startQueue = Math.max(1, baseQueue - currentIndex);

        for (int i = 0; i < 10; i++) {
            int queueNo = startQueue + i;
            String plate = String.format("%s %04d %s", i % 2 == 0 ? "B" : "L", 1000 + queueNo, i % 3 == 0 ? "EV" : "OK");
            String driver = DRIVER_NAMES[i % DRIVER_NAMES.length];
            String service = i % 2 == 0 ? "Charging" : "Service Ringan";
            int eta = Math.max(0, (i - currentIndex) * 5 + ThreadLocalRandom.current().nextInt(0, 4));
            String status = i == currentIndex ? "Sedang dipanggil" : (i < currentIndex ? "Selesai" : "Menunggu");

            if (i == currentIndex) {
                plate = controller.getSession().getPlateNumber() == null ? plate : controller.getSession().getPlateNumber();
                service = controller.getSession().getServiceType() == null ? service : controller.getSession().getServiceType().label();
                driver = "Anda";
            }

            queueModel.addElement(new QueueTicket(queueNo, plate, driver, service, eta, status));
        }
    }

    private void updateAnnouncement() {
        announcementLabel.setText("PENGUMUMAN: " + ANNOUNCEMENTS[announcementIndex % ANNOUNCEMENTS.length]);
    }

    private void rotateAnnouncement() {
        announcementIndex = (announcementIndex + 1) % ANNOUNCEMENTS.length;
        updateAnnouncement();
    }

    private void startTimers() {
        if (!flashTimer.isRunning()) {
            flashTimer.start();
        }
        if (!announcementTimer.isRunning()) {
            announcementTimer.start();
        }
    }

    private void stopTimers() {
        flashTimer.stop();
        announcementTimer.stop();
        flashOn = false;
    }

    private final class QueueCellRenderer extends JPanel implements ListCellRenderer<QueueTicket> {
        private final JLabel queueNoLabel = new JLabel();
        private final JLabel detailsLabel = new JLabel();
        private final JLabel statusBadge = new JLabel();
        private final JLabel etaLabel = new JLabel();

        private QueueCellRenderer() {
            setLayout(new BorderLayout(12, 6));
            setBorder(new EmptyBorder(12, 16, 12, 16));
            setOpaque(true);

            JPanel left = new JPanel(new BorderLayout(4, 4));
            left.setOpaque(false);
            queueNoLabel.setFont(UiKit.bigFont());
            detailsLabel.setFont(UiKit.normalFont().deriveFont(18f));
            left.add(queueNoLabel, BorderLayout.NORTH);
            left.add(detailsLabel, BorderLayout.SOUTH);

            JPanel right = new JPanel(new BorderLayout(4, 4));
            right.setOpaque(false);
            etaLabel.setFont(UiKit.normalFont().deriveFont(18f));
            etaLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            statusBadge.setHorizontalAlignment(SwingConstants.CENTER);
            statusBadge.setBorder(new EmptyBorder(8, 12, 8, 12));
            right.add(etaLabel, BorderLayout.NORTH);
            right.add(statusBadge, BorderLayout.SOUTH);

            add(left, BorderLayout.CENTER);
            add(right, BorderLayout.EAST);
        }

        @Override
        public java.awt.Component getListCellRendererComponent(JList<? extends QueueTicket> list, QueueTicket value, int index, boolean isSelected, boolean cellHasFocus) {
            boolean current = index == currentIndex;
            Color background;
            Color borderColor;

            if (current) {
                background = flashOn ? new Color(0xDDEBFF) : new Color(0xEEF5FF);
                borderColor = new Color(0x1E88E5);
            } else {
                background = Color.WHITE;
                borderColor = new Color(0xE5E7EB);
            }

            setBackground(background);
            setBorder(new LineBorder(borderColor, current ? 2 : 1, true));

            queueNoLabel.setText("Queue #" + value.queueNo());
            detailsLabel.setText(value.plate() + " • " + value.driver() + " • " + value.service());
            etaLabel.setText("ETA " + value.etaMinutes() + " min");
            statusBadge.setText(value.status());
            statusBadge.setOpaque(true);
            statusBadge.setBackground(current ? new Color(0x1E88E5) : new Color(0xECEFF1));
            statusBadge.setForeground(current ? Color.WHITE : new Color(0x455A64));

            return this;
        }
    }

    private record QueueTicket(int queueNo, String plate, String driver, String service, int etaMinutes, String status) {
    }
}
