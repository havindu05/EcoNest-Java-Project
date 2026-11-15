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
        styleComponents();

        btnAdd.addActionListener(e -> addClientToDatabase());
        btnRemove.addActionListener(e -> removeSelectedClient());
    }

    private void styleComponents() {
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);

        btnAdd.setOpaque(true);
        btnAdd.setContentAreaFilled(true);
        btnAdd.setBackground(new Color(46, 125, 50));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(btnFont);
        btnAdd.setFocusPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.setContentAreaFilled(true);
        btnAdd.setBorderPainted(false);

        btnRemove.setOpaque(true);
        btnRemove.setContentAreaFilled(true);
        btnRemove.setBackground(new Color(180, 50, 50));
        btnRemove.setForeground(Color.WHITE);
        btnRemove.setFont(btnFont);
        btnRemove.setFocusPainted(false);
        btnRemove.setOpaque(true);
        btnRemove.setContentAreaFilled(true);
        btnRemove.setBorderPainted(false);

        tableClients.setRowHeight(28);
        tableClients.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableClients.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableClients.getTableHeader().setBackground(new Color(27, 77, 62));
        tableClients.getTableHeader().setForeground(Color.WHITE);
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

    private void removeSelectedClient() {
        int selectedRow = tableClients.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel1, "Please select a client from the table to remove!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int clientId = Integer.parseInt(tableClients.getValueAt(selectedRow, 0).toString());
        String clientName = tableClients.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(panel1,
                "Are you sure you want to delete client '" + clientName + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM clients WHERE client_id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, clientId);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(panel1, "Client Removed Successfully!");
                loadClientData();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel1, "Cannot delete client (they may have existing orders): " + ex.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public JPanel getPanel1() {
        return panel1;
    }
}