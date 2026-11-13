package com.econest.model;

public class DeliveryAgent {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String vehicleNo;
    private String vehicleType;
    private String status;

    public DeliveryAgent(int id, String name, String phone, String email, String vehicleNo, String vehicleType, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.vehicleNo = vehicleNo;
        this.vehicleType = vehicleType;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getStatus() {
        return status;
    }
}