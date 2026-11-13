package com.econest.dao;

import com.econest.config.DBConnection;
import com.econest.model.Order;
import com.econest.util.EmailService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.order_id, o.client_id, c.name, o.total_amount FROM orders o JOIN clients c ON o.client_id = c.client_id";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("client_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("total_amount"),
                        "Dispatched"
                );
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean placeOrder(int clientId, int productId, int qty) {
        try (Connection connection = DBConnection.getConnection()) {
            //Get product price
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT price FROM products WHERE product_id = ?");
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return false;

            double price = resultSet.getDouble("price");
            double total = price * qty;

            //Insert into orders table
            PreparedStatement psOrder = connection.prepareStatement(
                    "INSERT INTO orders (client_id, order_date, total_amount) VALUES (?, CURDATE(), ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            psOrder.setInt(1, clientId);
            psOrder.setDouble(2, total);
            psOrder.executeUpdate();

            ResultSet genKeys = psOrder.getGeneratedKeys();
            if (genKeys.next()) {
                int orderId = genKeys.getInt(1);
                PreparedStatement psItem = connection.prepareStatement(
                        "INSERT INTO order_items (order_id, product_id, quantity, subtotal) VALUES (?, ?, ?, ?)"
                );
                psItem.setInt(1, orderId);
                psItem.setInt(2, productId);
                psItem.setInt(3, qty);
                psItem.setDouble(4, total);
                psItem.executeUpdate();
            }

            //Send email notification safely
            PreparedStatement psClient = connection.prepareStatement("SELECT email FROM clients WHERE client_id = ?");
            psClient.setInt(1, clientId);
            ResultSet resultSet1 = psClient.executeQuery();
            if (resultSet1.next()) {
                String email = resultSet1.getString("email");
                new Thread(() -> EmailService.sendNotification(
                        email,
                        "EcoNest Order Dispatched!",
                        "Your order has been dispatched. Total: LKR " + total
                )).start();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}