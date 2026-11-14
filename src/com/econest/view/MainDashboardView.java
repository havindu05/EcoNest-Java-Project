package com.econest.view;

import javax.swing.*;
import java.awt.*;

public class MainDashboardView extends JFrame {
    private JPanel panel1;
    private JButton productsStockButton;
    private JButton clientsDirectoryButton;
    private JButton clientOrdersButton;
    private JButton deliveryAgentsButton;
    private JButton monthlyReportsButton;
    private JPanel contentAreaPanel;

    private CardLayout cardLayout;

    public MainDashboardView() {
        setTitle("EcoNest - Home & Garden Retail Management System");
        setSize(1250, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(panel1);

        applyModernSidebarTheme();

        cardLayout = (CardLayout) contentAreaPanel.getLayout();

        contentAreaPanel.add(new ProductsPanel().getPanel1(), "PRODUCTS");
        contentAreaPanel.add(new ClientsPanel().getPanel1(), "CLIENTS");
        contentAreaPanel.add(new OrdersPanel().getPanel1(), "ORDERS");
        contentAreaPanel.add(new AgentsPanel().getPanel1(), "AGENTS");
        contentAreaPanel.add(new ReportsPanel().getPanel1(), "REPORTS");

        productsStockButton.addActionListener(e -> cardLayout.show(contentAreaPanel, "PRODUCTS"));
        clientsDirectoryButton.addActionListener(e -> cardLayout.show(contentAreaPanel, "CLIENTS"));
        clientOrdersButton.addActionListener(e -> cardLayout.show(contentAreaPanel, "ORDERS"));
        deliveryAgentsButton.addActionListener(e -> cardLayout.show(contentAreaPanel, "AGENTS"));
        monthlyReportsButton.addActionListener(e -> cardLayout.show(contentAreaPanel, "REPORTS"));
    }

    private void applyModernSidebarTheme() {
        Color sidebarBg = new Color(27, 77, 62);
        Color hoverBg = new Color(46, 125, 50);
        Font navFont = new Font("Segoe UI", Font.BOLD, 14);

        JButton[] navButtons = {
                productsStockButton, clientsDirectoryButton,
                clientOrdersButton, deliveryAgentsButton, monthlyReportsButton
        };

        for (JButton btn : navButtons) {
            if (btn != null) {

                btn.setOpaque(true);
                btn.setContentAreaFilled(true);

                btn.setBackground(sidebarBg);
                btn.setForeground(Color.WHITE);
                btn.setFont(navFont);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        btn.setBackground(hoverBg);
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        btn.setBackground(sidebarBg);
                    }
                });
            }
        }
    }
}