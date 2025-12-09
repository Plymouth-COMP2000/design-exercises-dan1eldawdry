package com.example.restaurantguestapp;

public class ReservationModel {

    private int id;
    private String username;
    private String date;
    private String time;
    private int groupSize;
    private String specialRequests;
    private String status;

    public ReservationModel(int id, String username, String date, String time,
                            int groupSize, String specialRequests, String status) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.time = time;
        this.groupSize = groupSize;
        this.specialRequests = specialRequests;
        this.status = status;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getGroupSize() { return groupSize; }
    public String getSpecialRequests() { return specialRequests; }
    public String getStatus() { return status; }
}
