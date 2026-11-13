package com.econest.model;

public class Order {
    private int orderId;
    private int clientId;
    private String clientName;
    private double totalAmount;
    private String status;

    public Order(int orderId, int clientId, String clientName, double totalAmount, String status) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.clientName = clientName;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }
}