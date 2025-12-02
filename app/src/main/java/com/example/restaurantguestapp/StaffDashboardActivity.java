package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StaffDashboardActivity extends AppCompatActivity {

    private Button logoutButton;
    private Button manageMenuButton;
    private Button viewReservationsButton;

    // static but will by dynamic fields
    private TextView newReservationsAmountText;
    private TextView totalMenuItemsAmountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        logoutButton = findViewById(R.id.button_logout);
        manageMenuButton = findViewById(R.id.button_manage_menu);
        viewReservationsButton = findViewById(R.id.button_view_reservations);
        newReservationsAmountText = findViewById(R.id.text_new_reservations_amount);
        totalMenuItemsAmountText = findViewById(R.id.text_total_menu_items_amount);

        setupButtonListeners();

        // placeholder to be replaced by database stuff later
        updateDashboardData();
    }

    private void setupButtonListeners() {

        logoutButton.setOnClickListener(v -> logoutUser());

        manageMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, ManageMenuActivity.class);
            startActivity(intent);
        });

        viewReservationsButton.setOnClickListener(v -> {
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

    // placeholder for future database implementation
    private void updateDashboardData() {
        // dynamic update logic
        // newReservationsAmountText.setText(String.valueOf(DatabaseHelper.getNewReservationsCount()));
        // totalMenuItemsAmountText.setText(String.valueOf(DatabaseHelper.getTotalMenuItemsCount()));

        newReservationsAmountText.setText("5");
        totalMenuItemsAmountText.setText("15");
    }
}