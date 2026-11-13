package com.econest.model;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private int stockQty;
    private int reorderLevel;

    public Product(int id, String name, String category, double price, int stockQty, int reorderLevel) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQty = stockQty;
        this.reorderLevel = reorderLevel;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQty() {
        return stockQty;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }
}