package com.econest.controller;

import com.econest.dao.OrderDAO;
import com.econest.model.Order;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class OrderController {
    private OrderDAO orderDAO = new OrderDAO();

    public void loadOrdersToTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Order> list = orderDAO.getAllOrders();

        for (int i = 0; i < list.size(); i++) {
            Order order = list.get(i);
            model.addRow(new Object[]{
                    order.getOrderId(), order.getClientName(), order.getTotalAmount(), order.getStatus()
            });
        }
    }

    public boolean createOrder(int clientId, int productId, int qty) {
        return orderDAO.placeOrder(clientId, productId, qty);
    }
}