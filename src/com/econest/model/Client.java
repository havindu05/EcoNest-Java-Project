package com.econest.model;

public class Client {
    private int id;
    private String name;
    private String clientType;
    private String phone;
    private String email;
    private String address;

    public Client(int id, String name, String clientType, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.clientType = clientType;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClientType() {
        return clientType;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
}