package com.example.restaurantguestapp;

public class NotificationModel {
    private int id;
    private String username;
    private String message;
    private long createdAt;

    public NotificationModel(int id, String username, String message, long createdAt) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getMessage() { return message; }
    public long getCreatedAt() { return createdAt; }
}
