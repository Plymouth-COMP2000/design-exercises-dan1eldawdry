package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    // bottom navigation buttons
    private Button menuButton;
    private Button reservationsButton;
    private Button logoutButton;

    private String username; // stores current users username


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        username = getIntent().getStringExtra("username"); // gets username that was passed through

        // link ui and java
        menuButton = findViewById(R.id.menuButton);
        reservationsButton = findViewById(R.id.reservationsButton);
        logoutButton = findViewById(R.id.logoutButton);

        setupButtons();
    }

    // click listeners for all buttons on screen
    private void setupButtons() {

        // open menu screen
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuListActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);

        });

        // open reservation screen
        reservationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyReservationsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // log user out
        logoutButton.setOnClickListener(v -> logoutUser());
    }

    // logs user out and returns them to login screen
    private void logoutUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears activity stack so cannot return
        startActivity(intent);
        finish();
    }
}