package com.serv.servgo.ui;

import com.serv.servgo.model.ScreenId;
import javax.swing.JComponent;

public interface ScreenView {
    ScreenId id();
    JComponent component();
    default void onShow() {
    }
}