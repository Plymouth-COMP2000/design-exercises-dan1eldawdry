package com.example.restaurantguestapp;

public class MenuItemModel {

    private int id;
    private String name;
    private String description;
    private String price;

    public MenuItemModel(int id, String name, String description, String price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }
}

