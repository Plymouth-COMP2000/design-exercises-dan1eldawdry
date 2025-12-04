package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ManageMenuActivity extends AppCompatActivity {

    private Button logoutButton;
    private Button addNewItemButton;
    private Button dashboardNavButton;
    private Button manageMenuNavButton;
    private Button viewReservationsNavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        logoutButton = findViewById(R.id.button_logout);
        addNewItemButton = findViewById(R.id.button_add_new_menu_item);
        dashboardNavButton = findViewById(R.id.button_nav_dashboard);
        manageMenuNavButton = findViewById(R.id.button_nav_manage_menu);
        viewReservationsNavButton = findViewById(R.id.button_nav_view_reservations);

        setupButtonListeners();
    }

    private void setupButtonListeners() {

        logoutButton.setOnClickListener(v -> logoutUser());

        addNewItemButton.setOnClickListener(v -> {
            // will add this later on
        });

        dashboardNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMenuActivity.this, StaffDashboardActivity.class);
            startActivity(intent);
            finish();
        });

        manageMenuNavButton.setOnClickListener(v -> {
        });

        viewReservationsNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManageMenuActivity.this, ViewReservationsActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void logoutUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}