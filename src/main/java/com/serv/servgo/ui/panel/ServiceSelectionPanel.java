package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.model.ServiceType;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ServiceSelectionPanel extends JPanel implements ScreenView {
    private final KioskController controller;
    private final JLabel slotLabel = new JLabel("", JLabel.CENTER);
    private final JLabel recLabel = new JLabel("", JLabel.CENTER);
    private final JLabel costLabel = new JLabel("", JLabel.CENTER);

    public ServiceSelectionPanel(KioskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(12, 12));
        setBorder(UiKit.screenPadding());

        add(UiKit.title("Pilih Layanan"), BorderLayout.NORTH);

        JPanel info = new JPanel(new java.awt.GridLayout(3, 1, 8, 8));
        slotLabel.setFont(UiKit.normalFont());
        recLabel.setFont(UiKit.normalFont());
        costLabel.setFont(UiKit.normalFont());
        info.add(slotLabel);
        info.add(recLabel);
        info.add(costLabel);
        add(info, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new java.awt.GridLayout(1, 4, 12, 12));
        buttons.add(UiKit.bigButton("Kembali", controller::goHome));
        buttons.add(UiKit.bigButton("⚡ Pengisian Daya", () -> controller.chooseService(ServiceType.CHARGING)));
        buttons.add(UiKit.bigButton("🔧 Servis Ringan", () -> controller.chooseService(ServiceType.LIGHT_SERVICE)));
        buttons.add(UiKit.bigButton("🗺 Maps", controller::showMaps));
        add(buttons, BorderLayout.SOUTH);
    }

    @Override
    public ScreenId id() {
        return ScreenId.SERVICE_SELECTION;
    }

    @Override
    public JComponent component() {
        return this;
    }

    @Override
    public void onShow() {
        int slots = controller.getQueueService().availableChargingSlots();
        controller.getSession().setChargingSlotsAvailable(slots);

        NumberFormat idr = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        slotLabel.setText("Live charging slots available: " + slots);
        recLabel.setText("Recommended charging level: " + controller.getSession().getRecommendedPercent() + "%");
        costLabel.setText("Charging starts from " + idr.format(ServiceType.CHARGING.baseCost()) +
                " | Light service starts from " + idr.format(ServiceType.LIGHT_SERVICE.baseCost()));
    }
}
