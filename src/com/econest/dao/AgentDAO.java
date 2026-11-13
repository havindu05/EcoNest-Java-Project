package com.econest.dao;

import com.econest.config.DBConnection;
import com.econest.model.DeliveryAgent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgentDAO {

    public List<DeliveryAgent> getAllAgents() {
        List<DeliveryAgent> list = new ArrayList<>();
        String sql = "SELECT * FROM delivery_agents";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                DeliveryAgent a = new DeliveryAgent(
                        resultSet.getInt("agent_id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("vehicle_no"),
                        resultSet.getString("vehicle_type"),
                        resultSet.getString("status")
                );
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addAgent(DeliveryAgent a) {
        String sql = "INSERT INTO delivery_agents (name, phone, email, vehicle_no, vehicle_type, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, a.getName());
            preparedStatement.setString(2, a.getPhone());
            preparedStatement.setString(3, a.getEmail());
            preparedStatement.setString(4, a.getVehicleNo());
            preparedStatement.setString(5, a.getVehicleType());
            preparedStatement.setString(6, a.getStatus());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}