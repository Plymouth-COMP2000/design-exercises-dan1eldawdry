package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyReservationsActivity extends AppCompatActivity {

    // navigation buttons
    private Button callButton;
    private Button menuButton;
    private Button logoutButton;
    private Button newReservationButton;

    private String username; // stores current users username

    // notifications ui
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private TextView noNotificationsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        username = getIntent().getStringExtra("username"); // gets the username passed through

        // link notification views
        notificationRecyclerView = findViewById(R.id.notificationsRecyclerView);
        noNotificationsText = findViewById(R.id.noNotificationsText);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // link ui and java
        logoutButton = findViewById(R.id.logoutButton);
        menuButton = findViewById(R.id.menuButton);
        callButton = findViewById(R.id.callButton);
        newReservationButton = findViewById(R.id.newReservationButton);

        setupButtons();
        setupNotifications();
        setupReservations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNotifications(); // refresh notifications when returns to the screen
    }

    // loads and displays guest notifications
    private void setupNotifications() {

        AppDatabaseHelper db = new AppDatabaseHelper(this);
        List<NotificationModel> notifications = db.getGuestNotifications(username);

        notificationAdapter = new NotificationAdapter(this, notifications);
        notificationRecyclerView.setAdapter(notificationAdapter);

        updateNotificationVisibility(notifications);
    }

    // reloads notifications when activity resumes
    private void refreshNotifications() {
        AppDatabaseHelper db = new AppDatabaseHelper(this);
        List<NotificationModel> notifications = db.getGuestNotifications(username);

        notificationAdapter.setNotifications(notifications);
        updateNotificationVisibility(notifications);
    }

    // shows or hide the no notifications message
    private void updateNotificationVisibility(List<NotificationModel> notifications) {

        if (notifications == null || notifications.isEmpty()) {
            noNotificationsText.setVisibility(View.VISIBLE);
        } else {
            noNotificationsText.setVisibility(View.GONE);
        }
    }

    // loads and displays guest reservations
    private void setupReservations() {

        AppDatabaseHelper db = new AppDatabaseHelper(this);
        ArrayList<ReservationModel> reservations = db.getReservationsForUser(username);

        ReservationAdapter adapter = new ReservationAdapter(this, reservations);

        RecyclerView reservationRecyclerView = findViewById(R.id.reservationsRecyclerView);

        reservationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationRecyclerView.setAdapter(adapter);
    }

    // click listeners for all buttons on screen
    private void setupButtons() {

        // opens new reservations screen
        newReservationButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReservationActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // opens menu list screen
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuListActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // opens call screen
        callButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
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