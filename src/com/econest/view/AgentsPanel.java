package com.econest.view;

import com.econest.config.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AgentsPanel {
    private JPanel panel1;
    private JTextField txtAgentName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtVehicleNo;
    private JComboBox cmbVehicleType;
    private JComboBox cmbStatus;
    private JButton btnAdd;
    private JTable tableAgents;

    public AgentsPanel() {
        loadAgentDropdowns();
        loadAgentData();
        styleComponents();

        btnAdd.addActionListener(e -> addAgentToDatabase());
    }

    private void styleComponents() {
        btnAdd.setBackground(new Color(46, 125, 50));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.setFocusPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.setContentAreaFilled(true);
        btnAdd.setBorderPainted(false);


        tableAgents.setRowHeight(28);
        tableAgents.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableAgents.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableAgents.getTableHeader().setBackground(new Color(27, 77, 62));
        tableAgents.getTableHeader().setForeground(Color.WHITE);
    }

    private void loadAgentDropdowns() {
        if (cmbVehicleType.getItemCount() == 0) {
            cmbVehicleType.addItem("Bike");
            cmbVehicleType.addItem("Three Wheeler");
            cmbVehicleType.addItem("Van");
            cmbVehicleType.addItem("Truck");
        }
        if (cmbStatus.getItemCount() == 0) {
            cmbStatus.addItem("Available");
            cmbStatus.addItem("On Delivery");
            cmbStatus.addItem("Offline");
        }
    }

    public void loadAgentData() {
        String[] columns = {"ID", "Name", "Phone", "Email", "Vehicle No", "Type", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String query = "SELECT agent_id AS id, name, phone, email, vehicle_no, vehicle_type, status FROM delivery_agents";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("vehicle_no"),
                        rs.getString("vehicle_type"),
                        rs.getString("status")
                });
            }
            tableAgents.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Error loading agents: " + ex.getMessage());
        }
    }

    private void addAgentToDatabase() {
        String name = txtAgentName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String vehicleNo = txtVehicleNo.getText().trim();
        String vehicleType = (cmbVehicleType.getSelectedItem() != null) ? cmbVehicleType.getSelectedItem().toString() : "Van";
        String status = (cmbStatus.getSelectedItem() != null) ? cmbStatus.getSelectedItem().toString() : "Available";

        if (name.isEmpty() || phone.isEmpty() || vehicleNo.isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Name, Phone, and Vehicle Number are required!");
            return;
        }

        String query = "INSERT INTO delivery_agents (name, phone, email, vehicle_no, vehicle_type, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setString(4, vehicleNo);
            pstmt.setString(5, vehicleType);
            pstmt.setString(6, status);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(panel1, "Delivery Agent Added Successfully!");

            txtAgentName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            txtVehicleNo.setText("");
            loadAgentData();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel1, "Database Error: " + ex.getMessage());
        }
    }

    public JPanel getPanel1() {
        return panel1;
    }
}