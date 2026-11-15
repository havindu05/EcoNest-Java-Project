package com.econest.view;

import com.econest.config.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class OrdersPanel {
    private JPanel panel1;
    private JTextField txtClientId;
    private JTextField txtProductId;
    private JTextField txtQty;
    private JButton btnOrder;
    private JTable tableOrders;

    public OrdersPanel() {
        loadOrderData();

        btnOrder.setBackground(new Color(40, 120, 50));
        btnOrder.setForeground(Color.WHITE);
        btnOrder.setOpaque(true);
        btnOrder.setContentAreaFilled(true);
        btnOrder.setBorderPainted(false);

        btnOrder.addActionListener(e -> createOrderAndDispatch());
    }

    public void loadOrderData() {
        String[] columns = {"Order ID", "Client Name", "Total Amount (LKR)", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String query = "SELECT o.order_id AS id, c.name AS client_name, o.total_amount, o.status " +
                "FROM orders o JOIN clients c ON o.client_id = c.client_id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("client_name"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                });
            }
            tableOrders.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Error loading orders: " + ex.getMessage());
        }
    }

    private void createOrderAndDispatch() {
        String clientIdStr = txtClientId.getText().trim();
        String productIdStr = txtProductId.getText().trim();
        String qtyStr = txtQty.getText().trim();

        if (clientIdStr.isEmpty() || productIdStr.isEmpty() || qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Please fill Client ID, Product ID, and Quantity!");
            return;
        }

        try {
            int clientId = Integer.parseInt(clientIdStr);
            int productId = Integer.parseInt(productIdStr);
            int qty = Integer.parseInt(qtyStr);

            try (Connection conn = DBConnection.getConnection()) {
                String priceQuery = "SELECT price, stock_qty FROM products WHERE product_id = ?";
                double price = 0.0;
                int currentStock = 0;

                try (PreparedStatement pstmtPrice = conn.prepareStatement(priceQuery)) {
                    pstmtPrice.setInt(1, productId);
                    try (ResultSet rs = pstmtPrice.executeQuery()) {
                        if (rs.next()) {
                            price = rs.getDouble("price");
                            currentStock = rs.getInt("stock_qty");
                        } else {
                            JOptionPane.showMessageDialog(panel1, "Product ID not found!");
                            return;
                        }
                    }
                }

                if (currentStock < qty) {
                    JOptionPane.showMessageDialog(panel1, "Insufficient stock available! Current stock: " + currentStock);
                    return;
                }

                double totalAmount = price * qty;

                // Updated table name to 'orders'
                String insertQuery = "INSERT INTO orders (client_id, product_id, quantity, total_amount, status) VALUES (?, ?, ?, ?, 'Dispatched')";
                try (PreparedStatement pstmtInsert = conn.prepareStatement(insertQuery)) {
                    pstmtInsert.setInt(1, clientId);
                    pstmtInsert.setInt(2, productId);
                    pstmtInsert.setInt(3, qty);
                    pstmtInsert.setDouble(4, totalAmount);
                    pstmtInsert.executeUpdate();
                }

                String updateStock = "UPDATE products SET stock_qty = stock_qty - ? WHERE product_id = ?";
                try (PreparedStatement pstmtStock = conn.prepareStatement(updateStock)) {
                    pstmtStock.setInt(1, qty);
                    pstmtStock.setInt(2, productId);
                    pstmtStock.executeUpdate();
                }

                JOptionPane.showMessageDialog(panel1, "Order Created & Dispatched Successfully! Total: LKR " + totalAmount);

                txtClientId.setText("");
                txtProductId.setText("");
                txtQty.setText("");
                loadOrderData();

            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel1, "Please enter valid numeric IDs and Quantity!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Database Error: " + ex.getMessage());
        }
    }

    public JPanel getPanel1() {
        return panel1;
    }
}