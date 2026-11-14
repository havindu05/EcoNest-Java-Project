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
        setSize(1150, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(panel1);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainDashboardView().setVisible(true);
        });
    }
}