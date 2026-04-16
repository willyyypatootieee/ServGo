package com.serv.servgo.app;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.SessionData;
import com.serv.servgo.service.QueueService;
import com.serv.servgo.ui.KioskFrame;
import com.serv.servgo.ui.panel.CheckInPanel;
import com.serv.servgo.ui.panel.MapsPanel;
import com.serv.servgo.ui.panel.MonitorPanel;
import com.serv.servgo.ui.panel.PaymentPanel;
import com.serv.servgo.ui.panel.QueuePanel;
import com.serv.servgo.ui.panel.ReceiptPanel;
import com.serv.servgo.ui.panel.ServiceSelectionPanel;
import com.serv.servgo.ui.panel.TurnAlertPanel;
import com.serv.servgo.ui.panel.WelcomePanel;
import javax.swing.SwingUtilities;

public final class KioskApp {
    private KioskApp() {
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            SessionData session = new SessionData();
            QueueService queueService = new QueueService();

            KioskFrame frame = new KioskFrame();
            KioskController controller = new KioskController(frame, session, queueService);

            WelcomePanel welcomePanel = new WelcomePanel(controller);
            CheckInPanel checkInPanel = new CheckInPanel(controller);
            ServiceSelectionPanel servicePanel = new ServiceSelectionPanel(controller);
            QueuePanel queuePanel = new QueuePanel(controller);
            TurnAlertPanel alertPanel = new TurnAlertPanel(controller);
            MonitorPanel monitorPanel = new MonitorPanel(controller);
            PaymentPanel paymentPanel = new PaymentPanel(controller);
            ReceiptPanel receiptPanel = new ReceiptPanel(controller);
            MapsPanel mapsPanel = new MapsPanel(controller);

            controller.setMonitorPanel(monitorPanel);

            frame.register(welcomePanel);
            frame.register(checkInPanel);
            frame.register(servicePanel);
            frame.register(queuePanel);
            frame.register(alertPanel);
            frame.register(monitorPanel);
            frame.register(paymentPanel);
            frame.register(receiptPanel);
            frame.register(mapsPanel);

            frame.showScreen(com.serv.servgo.model.ScreenId.WELCOME);
            frame.setVisible(true);
        });
    }
}