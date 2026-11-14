package com.econest.view;

import com.econest.config.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ClientsPanel {
    private JPanel panel1;
    private JTextField txtName;
    private JComboBox cmbCategory;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JButton btnAdd;
    private JButton btnRemove;
    private JTable tableClients;

    public ClientsPanel() {
        loadClientCategories();
        loadClientData();

        btnAdd.setBackground(new Color(40, 120, 50));
        btnAdd.setForeground(Color.WHITE);
        btnRemove.setBackground(new Color(180, 50, 50));
        btnRemove.setForeground(Color.WHITE);

        btnAdd.addActionListener(e -> addClientToDatabase());
    }

    private void loadClientCategories() {
        if (cmbCategory.getItemCount() == 0) {
            cmbCategory.addItem("Household");
            cmbCategory.addItem("Corporate");
            cmbCategory.addItem("Wholesale");
        }
    }

    public void loadClientData() {
        String[] columns = {"ID", "Name", "Type", "Phone", "Email", "Address"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String query = "SELECT client_id AS id, name, client_type, phone, email, address FROM clients";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("client_type"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                });
            }
            tableClients.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Error loading clients: " + ex.getMessage());
        }
    }

    private void addClientToDatabase() {
        String name = txtName.getText().trim();
        String type = (cmbCategory.getSelectedItem() != null) ? cmbCategory.getSelectedItem().toString() : "Household";
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Name and Phone are required!");
            return;
        }

        String query = "INSERT INTO clients (name, client_type, phone, email, address) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.setString(5, address);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(panel1, "Client Added Successfully!");

            txtName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            txtAddress.setText("");
            loadClientData();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Database Error: " + ex.getMessage());
        }
    }

    public JPanel getPanel1() {
        return panel1;
    }
}