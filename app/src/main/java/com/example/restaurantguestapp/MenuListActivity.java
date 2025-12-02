package com.example.restaurantguestapp;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuListActivity extends AppCompatActivity {

    private Button reservationsButton;
    private Button callButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        // find bottom nav buttons
        reservationsButton = findViewById(R.id.button_reservations);
        callButton = findViewById(R.id.button_call);
        logoutButton = findViewById(R.id.button_logout)

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        // listner for reservations
        reservationsButton.setOnClickListener(v -> {
            startActivity(new Intent(MenuListActivity.this, MyReservationsActivity.class));
        });

        // listener for call
        callButton.setOnClickListener(v -> {
            startActivity(new Intent(MenuListActivity.this, ContactActivity.class));
        });

        logoutButton.setOnClickListener(v -> logoutUser());
    }
    // logout method
    private void logoutUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        // clear previous activites and start new screen
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}