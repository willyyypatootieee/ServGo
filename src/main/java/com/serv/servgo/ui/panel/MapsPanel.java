package com.serv.servgo.ui.panel;

import com.serv.servgo.controller.KioskController;
import com.serv.servgo.model.ScreenId;
import com.serv.servgo.ui.ScreenView;
import com.serv.servgo.ui.UiKit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MapsPanel extends JPanel implements ScreenView {
    private final MapImagePanel mapImagePanel = new MapImagePanel("/Main.png", "/MapsIndo.png");
    private final JButton toggleButton;
    private boolean showingEastJavaMap;

    public MapsPanel(KioskController controller) {
        setLayout(new BorderLayout(12, 12));
        setBorder(UiKit.screenPadding());

        add(UiKit.title("Rest Area Map"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBorder(UiKit.screenPadding());

        JLabel hint = UiKit.normal("Lihat peta lokasi utama, lalu buka peta area Jawa Timur.");
        center.add(hint, BorderLayout.NORTH);
        center.add(new JScrollPane(mapImagePanel), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        toggleButton = UiKit.bigButton("Lihat ServGo di Seluruh pulau Jatim", this::toggleMap);

        JPanel actions = new JPanel(new BorderLayout(12, 12));
        actions.add(toggleButton, BorderLayout.CENTER);
        actions.add(UiKit.bigButton("Back", controller::backToServiceSelection), BorderLayout.SOUTH);
        add(actions, BorderLayout.SOUTH);
    }

    @Override
    public ScreenId id() {
        return ScreenId.MAPS;
    }

    @Override
    public JComponent component() {
        return this;
    }

    private void toggleMap() {
        showingEastJavaMap = !showingEastJavaMap;
        mapImagePanel.showResource(showingEastJavaMap ? "/MapsIndo.png" : "/Main.png");
        toggleButton.setText(showingEastJavaMap
                ? "Lihat Peta ServGo Utama"
                : "Lihat ServGo di Seluruh pulau Jatim");
    }

    private static final class MapImagePanel extends JPanel {
        private final String fallbackOne;
        private final String fallbackTwo;
        private BufferedImage image;
        private String currentResource;

        private MapImagePanel(String fallbackOne, String fallbackTwo) {
            this.fallbackOne = fallbackOne;
            this.fallbackTwo = fallbackTwo;
            setOpaque(true);
            setBackground(new Color(0xFAFAFB));
            setPreferredSize(new Dimension(900, 520));
            showResource(fallbackOne);
        }

        private void showResource(String resourcePath) {
            currentResource = resourcePath;
            image = readImage(resourcePath);
            if (image == null && !Objects.equals(resourcePath, fallbackTwo)) {
                image = readImage(fallbackTwo);
                currentResource = fallbackTwo;
            }
            if (image == null && !Objects.equals(resourcePath, fallbackOne)) {
                image = readImage(fallbackOne);
                currentResource = fallbackOne;
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int w = getWidth();
                int h = getHeight();
                int pad = 18;

                g2.setColor(getBackground());
                g2.fillRect(0, 0, w, h);

                if (image == null) {
                    g2.setColor(new Color(0x666666));
                    g2.drawString("Map image not found: " + currentResource, pad, pad + 20);
                    return;
                }

                double scale = Math.min((double) (w - pad * 2) / image.getWidth(), (double) (h - pad * 2) / image.getHeight());
                int drawW = (int) Math.round(image.getWidth() * scale);
                int drawH = (int) Math.round(image.getHeight() * scale);
                int x = (w - drawW) / 2;
                int y = (h - drawH) / 2;

                g2.drawImage(image, x, y, drawW, drawH, null);
            } finally {
                g2.dispose();
            }
        }

        private BufferedImage readImage(String resourcePath) {
            try (InputStream in = MapsPanel.class.getResourceAsStream(resourcePath)) {
                if (in != null) {
                    return ImageIO.read(in);
                }
            } catch (IOException ignored) {
                // fall through to fallback attempt / error message
            }
            return null;
        }
    }
}
