package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ViewReservationsActivity extends AppCompatActivity {

    private Button logoutButton;
    private Button dashboardNavButton;
    private Button manageMenuNavButton;
    private Button viewReservationsNavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservations);

        logoutButton = findViewById(R.id.button_logout);
        dashboardNavButton = findViewById(R.id.button_nav_dashboard);
        manageMenuNavButton = findViewById(R.id.button_nav_manage_menu);
        viewReservationsNavButton = findViewById(R.id.button_nav_view_reservations);

        setupButtonListeners();
        // methods to get and display reservations will go here
    }

    private void setupButtonListeners() {

        logoutButton.setOnClickListener(v -> logoutUser());

        dashboardNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewReservationsActivity.this, StaffDashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // BOTTOM NAVIGATION: Manage Menu Button
        manageMenuNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewReservationsActivity.this, ManageMenuActivity.class);
            startActivity(intent);
            finish();
        });

        viewReservationsNavButton.setOnClickListener(v -> {
            // already on no changes needed
        });
    }

    private void logoutUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}