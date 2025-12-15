package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StaffDashboardActivity extends AppCompatActivity {

    private Button logoutButton;
    private Button manageMenuNavButton;
    private Button viewReservationsNavButton;
    private Button dashboardNavButton;

    // static but will by dynamic fields
    private TextView newReservationsAmountText;
    private TextView totalMenuItemsAmountText;
    private AppDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        logoutButton = findViewById(R.id.button_logout);
        manageMenuNavButton = findViewById(R.id.button_nav_manage_menu);
        dashboardNavButton = findViewById(R.id.button_nav_dashboard);
        viewReservationsNavButton = findViewById(R.id.button_nav_view_reservations);
        newReservationsAmountText = findViewById(R.id.text_new_reservations_amount);
        totalMenuItemsAmountText = findViewById(R.id.text_total_menu_items_amount);
        db = new AppDatabaseHelper(this);
        setupButtonListeners();

        loadReservationCount();
    }

    // makes sure it updates when i return to the dashboard
    @Override
    protected void onResume() {
        super.onResume();
        loadReservationCount();
    }


    private void setupButtonListeners() {

        logoutButton.setOnClickListener(v -> logoutUser());

        if (dashboardNavButton != null) {
            dashboardNavButton.setOnClickListener(v -> {
                // already on, dont do anything
            });
        }

        manageMenuNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, ManageMenuActivity.class);
            startActivity(intent);
        });

        viewReservationsNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, ViewReservationsActivity.class);
            startActivity(intent);
        });
    }

    // logout method
    private void logoutUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        // clear previous activites and start new screen
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loadReservationCount() {
        int count = db.getReservationCount();
        newReservationsAmountText.setText(String.valueOf(count));
    }

    private void loadMenuItemCount() {
        int itemCount = db.getMenuItemCount();
        totalMenuItemsAmountText.setText(String.valueOf(itemCount));
    }

}