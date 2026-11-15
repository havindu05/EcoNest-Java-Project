package com.econest.view;

import com.econest.config.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReportsPanel {
    private JPanel panel1;
    private JTextArea txtReportArea;
    private JButton btnGenerate;

    public ReportsPanel() {
        txtReportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtReportArea.setEditable(false);

        btnGenerate.setBackground(new Color(40, 120, 50));
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setOpaque(true);
        btnGenerate.setContentAreaFilled(true);
        btnGenerate.setBorderPainted(false);

        generateLiveReport();
        btnGenerate.addActionListener(e -> generateLiveReport());
    }

    private void generateLiveReport() {
        int totalOrders = 0;
        double totalRevenue = 0.0;
        StringBuilder lowStockAlerts = new StringBuilder();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Updated table name to 'orders'
            ResultSet rsOrders = stmt.executeQuery("SELECT COUNT(*) AS total_orders, SUM(total_amount) AS revenue FROM orders");
            if (rsOrders.next()) {
                totalOrders = rsOrders.getInt("total_orders");
                totalRevenue = rsOrders.getDouble("revenue");
            }

            ResultSet rsStock = stmt.executeQuery("SELECT name, stock_qty, reorder_level FROM products WHERE stock_qty <= reorder_level");
            boolean hasLowStock = false;
            while (rsStock.next()) {
                hasLowStock = true;
                lowStockAlerts.append("  [ALERT] ")
                        .append(rsStock.getString("name"))
                        .append(" (Stock: ").append(rsStock.getInt("stock_qty"))
                        .append(" | Reorder Level: ").append(rsStock.getInt("reorder_level"))
                        .append(")\n");
            }

            if (!hasLowStock) {
                lowStockAlerts.append("  All inventory stock levels are optimal.\n");
            }

            String reportText =
                    "====================================================\n" +
                            "            ECONEST RETAIL MANAGEMENT REPORT        \n" +
                            "====================================================\n\n" +
                            "1. SALES REVENUE SUMMARY:\n" +
                            "   - Total Orders Processed : " + totalOrders + "\n" +
                            "   - Total Revenue Generated: LKR " + String.format("%.2f", totalRevenue) + "\n\n" +
                            "2. LOW-STOCK INVENTORY ALERTS:\n" +
                            lowStockAlerts.toString() + "\n" +
                            "====================================================\n" +
                            "Report generated successfully for EcoNest Operations.";

            txtReportArea.setText(reportText);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Error generating report: " + ex.getMessage());
        }
    }

    public JPanel getPanel1() {
        return panel1;
    }
}