package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    private Button menuButton;
    private Button reservationsButton;
    private Button callButton;
    private Button logoutButton;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        username = getIntent().getStringExtra("username"); // gets username that was passed through

        // bottom navigation buttons
        menuButton = findViewById(R.id.button_menu);
        reservationsButton = findViewById(R.id.button_reservations);
        callButton = findViewById(R.id.button_call);
        logoutButton = findViewById(R.id.button_logout);

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        // listener for menu
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuListActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);

        });

        // listener for reservation
        reservationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyReservationsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // listener for call
        callButton.setOnClickListener(v -> {
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