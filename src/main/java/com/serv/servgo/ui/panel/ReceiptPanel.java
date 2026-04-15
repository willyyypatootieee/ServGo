package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ReceiptPanel extends JPanel implements ScreenView {
    private final KioskController controller;
    private final JTextArea receiptArea = UiKit.readOnlyArea();

    public ReceiptPanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(UiKit.title("Digital Kwitansi"), BorderLayout.NORTH);
        add(new JScrollPane(receiptArea), BorderLayout.CENTER);

        JPanel actions = new JPanel(new java.awt.GridLayout(1, 2, 12, 12));
        actions.add(UiKit.bigButton("Copy Receipt", this::copyReceipt));
        actions.add(UiKit.bigButton("Selesai", controller::goHome));
        add(actions, BorderLayout.SOUTH);
    }

    @Override
    public ScreenId id() {
        return ScreenId.RECEIPT;
    }

    @Override
    public JComponent component() {
        return this;
    }

    @Override
    public void onShow() {
        receiptArea.setText(controller.buildReceiptText());
        receiptArea.setCaretPosition(0);
    }

    private void copyReceipt() {
        String text = receiptArea.getText();
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }
}
