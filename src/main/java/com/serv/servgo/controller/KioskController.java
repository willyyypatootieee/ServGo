package com.serv.servgo.controller;

import com.serv.servgo.model.PaymentMethod;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.model.ServiceType;
import com.serv.servgo.model.SessionData;
import com.serv.servgo.service.QueueService;
import com.serv.servgo.ui.KioskFrame;
import com.serv.servgo.ui.panel.MonitorPanel;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.Timer;

public class KioskController {
    private final KioskFrame frame;
    private final SessionData session;
    private final QueueService queueService;
    private MonitorPanel monitorPanel;

    private Timer progressTimer;
    private Timer resetTimer;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

    public KioskController(KioskFrame frame, SessionData session, QueueService queueService) {
        this.frame = frame;
        this.session = session;
        this.queueService = queueService;
    }

    public SessionData getSession() {
        return session;
    }

    public QueueService getQueueService() {
        return queueService;
    }

    public void setMonitorPanel(MonitorPanel monitorPanel) {
        this.monitorPanel = monitorPanel;
    }

    public void goHome() {
        stopTimers();
        session.reset();
        frame.showScreen(ScreenId.WELCOME);
    }

    public void startSession() {
        stopTimers();
        session.reset();
        frame.showScreen(ScreenId.CHECK_IN);
    }

    public boolean submitPlate(String rawPlate) {
        String plate = sanitizePlate(rawPlate);
        if (plate.length() < 4) {
            return false;
        }
        session.setPlateNumber(plate);
        frame.showScreen(ScreenId.SERVICE_SELECTION);
        return true;
    }

    public void chooseService(ServiceType type) {
        session.setServiceType(type);
        session.setQueueNumber(queueService.nextQueueNumber(type));
        session.setEstimatedWaitMinutes(queueService.estimateWaitMinutes());
        session.setChargingSlotsAvailable(queueService.availableChargingSlots());
        session.setProgressPercent(0);
        frame.showScreen(ScreenId.QUEUE);
    }

    public void showMaps() {
        frame.showScreen(ScreenId.MAPS);
    }

    public void backToServiceSelection() {
        frame.showScreen(ScreenId.SERVICE_SELECTION);
    }

    public void notifyTurn() {
        Toolkit.getDefaultToolkit().beep();
        frame.showScreen(ScreenId.ALERT);
    }

    public void startProcess() {
        stopProgressTimer();
        session.setProgressPercent(0);
        if (monitorPanel != null) {
            monitorPanel.setProgress(0, Math.max(0, session.getEstimatedWaitMinutes()));
        }
        frame.showScreen(ScreenId.MONITOR);

        progressTimer = new Timer(180, e -> {
            int next = Math.min(100, session.getProgressPercent() + 2);
            session.setProgressPercent(next);

            int etaMinutes = Math.max(0, (100 - next) / 20);
            if (monitorPanel != null) {
                monitorPanel.setProgress(next, etaMinutes);
            }

            if (next >= 100) {
                stopProgressTimer();
                showPayment();
            }
        });
        progressTimer.start();
    }

    public void confirmPayment(PaymentMethod method) {
        session.setPaymentMethod(method == null ? PaymentMethod.QRIS : method);
        frame.showScreen(ScreenId.RECEIPT);
        scheduleAutoReset();
    }

    public String buildReceiptText() {
        NumberFormat idr = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));
        String plate = session.getPlateNumber() == null ? "-" : session.getPlateNumber();
        String service = session.getServiceType() == null ? "-" : session.getServiceType().label();
        String payment = session.getPaymentMethod() == null ? "-" : session.getPaymentMethod().name();

        return """
               ===== EV REST AREA RECEIPT =====
               Time          : %s
               Plate         : %s
               Service       : %s
               Queue Number  : %d
               Est. Wait     : %d min
               Payment       : %s
               Slots         : %d
               Recommended   : %d%%
               Total         : %s
               ================================
               Thank you for using ServGo
               """.formatted(
                LocalDateTime.now().format(dateTimeFormatter),
                plate,
                service,
                session.getQueueNumber(),
                session.getEstimatedWaitMinutes(),
                payment,
                session.getChargingSlotsAvailable(),
                session.getRecommendedPercent(),
                idr.format(session.totalCost())
        );
    }

    private void showPayment() {
        frame.showScreen(ScreenId.PAYMENT);
    }

    private void scheduleAutoReset() {
        if (resetTimer != null) {
            resetTimer.stop();
        }
        resetTimer = new Timer(8000, e -> goHome());
        resetTimer.setRepeats(false);
        resetTimer.start();
    }

    private void stopTimers() {
        stopProgressTimer();
        if (resetTimer != null) {
            resetTimer.stop();
            resetTimer = null;
        }
    }

    private void stopProgressTimer() {
        if (progressTimer != null) {
            progressTimer.stop();
            progressTimer = null;
        }
    }

    private String sanitizePlate(String rawPlate) {
        if (rawPlate == null) {
            return "";
        }
        return rawPlate.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "");
    }
}