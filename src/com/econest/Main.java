package com.econest;

import com.econest.view.MainDashboardView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainDashboardView dashboard = new MainDashboardView();
            dashboard.setVisible(true);
        });
    }
}