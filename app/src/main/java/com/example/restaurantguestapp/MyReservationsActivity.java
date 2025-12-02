package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyReservationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        setupNavigationAndActions();

        // load users existing rservations when screen opens
        fetchAndDisplayReservations();
    }

    private void setupNavigationAndActions() {
        Button newReservationButton = findViewById(R.id.button_new_reservation);
        Button menuNavButton = findViewById(R.id.button_menu_nav);
        Button callNavButton = findViewById(R.id.button_call_nav);

        // new reservation nav
        newReservationButton.setOnClickListener(v -> {
            startActivity(new Intent(MyReservationsActivity.this, ReservationActivity.class));
        });

        // menu list nav
        menuNavButton.setOnClickListener(v -> {
            startActivity(new Intent(MyReservationsActivity.this, MenuListActivity.class));
            finish();
        });

        // contact screen nav
        callNavButton.setOnClickListener(v -> {
            startActivity(new Intent(MyReservationsActivity.this, ContactActivity.class));
        });
    }

    // placeholder method for getting the users reservations
    private void fetchAndDisplayReservations() {
        // where I connect to my database like appdatabasehelper
        // get reservation records and update list
        Toast.makeText(this, "reservations fetched and displayed", Toast.LENGTH_SHORT).show();
    }
}