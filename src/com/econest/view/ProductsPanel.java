package com.econest.view;

import com.econest.config.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ProductsPanel {
    private JPanel panel1;
    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtQty;
    private JTextField txtReorder;
    private JComboBox cmbCategory;
    private JComboBox cmbEco;
    private JButton btnAdd;
    private JButton btnRemove;
    private JTable tableProducts;

    public ProductsPanel() {
        loadCategories();
        loadEcoRatings();
        loadTableData();
        styleComponents();

        btnAdd.addActionListener(e -> addProductToDatabase());
    }

    private void styleComponents() {
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        btnAdd.setBackground(new Color(46, 125, 50));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(btnFont);
        btnAdd.setFocusPainted(false);

        btnRemove.setBackground(new Color(180, 50, 50));
        btnRemove.setForeground(Color.WHITE);
        btnRemove.setFont(btnFont);
        btnRemove.setFocusPainted(false);

        // Table Pro Styling
        tableProducts.setRowHeight(28);
        tableProducts.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableProducts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableProducts.getTableHeader().setBackground(new Color(27, 77, 62));
        tableProducts.getTableHeader().setForeground(Color.WHITE);
    }

    private void loadCategories() {
        if (cmbCategory.getItemCount() == 0) {
            cmbCategory.addItem("Plant");
            cmbCategory.addItem("Seeds");
            cmbCategory.addItem("Fertilizer");
            cmbCategory.addItem("Tools");
            cmbCategory.addItem("Household");
        }
    }

    private void loadEcoRatings() {
        if (cmbEco.getItemCount() == 0) {
            cmbEco.addItem("1");
            cmbEco.addItem("2");
            cmbEco.addItem("3");
            cmbEco.addItem("4");
            cmbEco.addItem("5");
        }
    }

    public void loadTableData() {
        String[] columns = {"ID", "Name", "Category", "Price (LKR)", "Eco Rating", "Stock", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String query = "SELECT product_id AS id, name, category, price, eco_rating, stock_qty, reorder_level FROM products";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                int ecoRating = rs.getInt("eco_rating");
                int stock = rs.getInt("stock_qty");
                int reorder = rs.getInt("reorder_level");

                String status = (stock <= reorder) ? "LOW STOCK" : "In Stock";

                model.addRow(new Object[]{id, name, category, price, ecoRating + " ", stock, status});
            }
            tableProducts.setModel(model);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProductToDatabase() {
        String name = txtName.getText().trim();
        String category = (cmbCategory.getSelectedItem() != null) ? cmbCategory.getSelectedItem().toString() : "Plant";
        String priceStr = txtPrice.getText().trim();
        String ecoStr = (cmbEco.getSelectedItem() != null) ? cmbEco.getSelectedItem().toString() : "5";
        String qtyStr = txtQty.getText().trim();
        String reorderStr = txtReorder.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Please fill all required fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int eco = Integer.parseInt(ecoStr);
            int qty = Integer.parseInt(qtyStr);
            int reorder = reorderStr.isEmpty() ? 10 : Integer.parseInt(reorderStr);

            String query = "INSERT INTO products (name, category, price, eco_rating, stock_qty, reorder_level) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, name);
                pstmt.setString(2, category);
                pstmt.setDouble(3, price);
                pstmt.setInt(4, eco);
                pstmt.setInt(5, qty);
                pstmt.setInt(6, reorder);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(panel1, "Product Added Successfully!");

                txtName.setText("");
                txtPrice.setText("");
                txtQty.setText("");
                txtReorder.setText("");
                loadTableData();

            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel1, "Please enter valid numeric values for Price/Stock!", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Database Insert Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel1() {
        return panel1;
    }
}