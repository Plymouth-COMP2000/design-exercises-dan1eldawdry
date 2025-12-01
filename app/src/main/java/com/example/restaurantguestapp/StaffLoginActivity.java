package com.example.restaurantguestapp;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class StaffLoginActivity extends AppCompatActivity {

    private Button backButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        backButton = findViewById(R.id.button_back_to_guest_login);
        loginButton = findViewById(R.id.button_staff_login_submit);

        backButton.setOnClickListener(v -> {
            finish();
        });

        // TO DO: add actual staff login here
        loginButton.setOnClickListener(v -> {
        });
    }
}