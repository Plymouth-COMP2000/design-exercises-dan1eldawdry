package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewReservationsActivity extends AppCompatActivity {

    // navigation buttons
    private Button logoutButton;
    private Button dashboardButton;
    private Button manageMenuButton;

    private String username; // stores current users username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservations);

        username = getIntent().getStringExtra("username"); // gets the username passed through

        // link ui and java
        logoutButton = findViewById(R.id.logoutButton);
        dashboardButton = findViewById(R.id.dashboardButton);
        manageMenuButton = findViewById(R.id.manageMenuButton);

        setupButtons();
        setupReservations();

    }

    // loads and displays guest reservations
    private void setupReservations() {

        AppDatabaseHelper db = new AppDatabaseHelper(this);
        ArrayList<ReservationModel> reservations = db.getAllReservations();

        ReservationAdapter adapter = new ReservationAdapter(this, reservations);

        RecyclerView reservationRecyclerView = findViewById(R.id.reservationsRecyclerView);

        reservationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationRecyclerView.setAdapter(adapter);
    }

    // click listeners for all buttons on screen
    private void setupButtons() {

        // opens dashboard screen
        dashboardButton.setOnClickListener(v -> {
            startActivity(new Intent(this, StaffDashboardActivity.class));
            finish();
        });

        // opens manage menu button
        manageMenuButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageMenuActivity.class));
            finish();
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
}