package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.PaymentMethod;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class PaymentPanel extends JPanel implements ScreenView {
    private final KioskController controller;
    private final JTextArea summaryArea = UiKit.readOnlyArea();
    private final JRadioButton qris = new JRadioButton("QRIS");
    private final JRadioButton cash = new JRadioButton("Cash");
    private final JRadioButton card = new JRadioButton("Card");

    public PaymentPanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(UiKit.title("Pembayaran"), BorderLayout.NORTH);

        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        add(summaryArea, BorderLayout.CENTER);

        JPanel methods = new JPanel(new java.awt.GridLayout(1, 3, 8, 8));
        ButtonGroup group = new ButtonGroup();
        group.add(qris);
        group.add(cash);
        group.add(card);
        methods.add(qris);
        methods.add(cash);
        methods.add(card);

        JPanel south = new JPanel(new BorderLayout(8, 8));
        south.add(methods, BorderLayout.CENTER);
        south.add(UiKit.bigButton("Pay Now", () -> controller.confirmPayment(selectedMethod())), BorderLayout.SOUTH);

        add(south, BorderLayout.SOUTH);
    }

    @Override
    public ScreenId id() {
        return ScreenId.PAYMENT;
    }

    @Override
    public JComponent component() {
        return this;
    }

    @Override
    public void onShow() {
        qris.setSelected(true);
        summaryArea.setText(buildSummary());
    }

    private PaymentMethod selectedMethod() {
        if (cash.isSelected()) {
            return PaymentMethod.CASH;
        }
        if (card.isSelected()) {
            return PaymentMethod.CARD;
        }
        return PaymentMethod.QRIS;
    }

    private String buildSummary() {
        var session = controller.getSession();
        NumberFormat idr = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String service = session.getServiceType() == null ? "-" : session.getServiceType().label();
        return """
               Payment Summary
               ----------------
               Plat Nomor         : %s
               Servis             : %s
               Antrian            : %d
               Waktu Tunggu       : %d min
               Waktu Isi          : %d%%
               Total      : %s
               """.formatted(
                session.getPlateNumber(),
                service,
                session.getQueueNumber(),
                session.getEstimatedWaitMinutes(),
                session.getRecommendedPercent(),
                idr.format(session.totalCost())
        );
    }
}
