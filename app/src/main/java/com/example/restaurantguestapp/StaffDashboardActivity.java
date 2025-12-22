package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StaffDashboardActivity extends AppCompatActivity {

    // // navigation buttons
    private Button logoutButton;
    private Button manageMenuButton;
    private Button viewReservationsButton;

    // dashbaord stats
    private TextView reservationCountText;
    private TextView menuItemCountText;

    private AppDatabaseHelper db; // database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        // link ui and java
        manageMenuButton = findViewById(R.id.manageMenuButton);
        viewReservationsButton = findViewById(R.id.viewReservationsButton);
        logoutButton = findViewById(R.id.logoutButton);
        reservationCountText = findViewById(R.id.reservationCountText);
        menuItemCountText = findViewById(R.id.menuItemCountText);

        db = new AppDatabaseHelper(this);

        setupNotifications();

        loadReservationCount();
        loadMenuItemCount();

        setupButtons();
    }

    // makes sure it updates when i return to the dashboard
    @Override
    protected void onResume() {
        super.onResume();
        loadReservationCount();
        loadMenuItemCount();
    }

    // sets up recyclerview for notifications
    private void setupNotifications() {

        RecyclerView notificationRecyclerView = findViewById(R.id.notificationsRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<NotificationModel> notifications = db.getStaffNotifications();

        NotificationAdapter adapter = new NotificationAdapter(this, notifications);
        notificationRecyclerView.setAdapter(adapter);
    }

    // click listeners for all buttons on screen
    private void setupButtons() {

        // opens manage menu screen
        manageMenuButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageMenuActivity.class));
        });

        //opens view reservations screen
        viewReservationsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewReservationsActivity.class));
        });

        // log user out
        logoutButton.setOnClickListener(v -> logoutUser());
    }

    private void logoutUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear previous activites and start new screen
        startActivity(intent);
        finish();
    }

    // load and display number of reservations
    private void loadReservationCount() {
        int count = db.getReservationCount();
        reservationCountText.setText(String.valueOf(count));
    }

    //load and display number of menu items
    private void loadMenuItemCount() {
        int itemCount = db.getMenuItemCount();
        menuItemCountText.setText(String.valueOf(itemCount));
    }

}