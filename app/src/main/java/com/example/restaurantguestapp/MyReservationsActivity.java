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

    private Button logoutButton;
    String username;
    private RecyclerView recyclerViewNotifications;
    private NotificationAdapter notificationAdapter;
    private TextView textNoNotifications;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        username = getIntent().getStringExtra("username"); // gets the username passed through

        recyclerViewNotifications = findViewById(R.id.recycler_view_guest_notifications);
        textNoNotifications = findViewById(R.id.text_no_guest_notifications);

        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));


        logoutButton = findViewById(R.id.button_logout);
        setupNavigationAndActions();

        // load users existing rservations when screen opens
        fetchAndDisplayReservations();

        AppDatabaseHelper db = new AppDatabaseHelper(this);
        List<NotificationModel> notifications = db.getGuestNotifications(username);

        notificationAdapter = new NotificationAdapter(this, notifications);
        recyclerViewNotifications.setAdapter(notificationAdapter);

        if (notifications == null || notifications.isEmpty()) {
            textNoNotifications.setVisibility(View.VISIBLE);
        } else {
            textNoNotifications.setVisibility(View.GONE);
        }

        ArrayList<ReservationModel> list = db.getReservationsForUser(username);

        ReservationAdapter adapter = new ReservationAdapter(this, list);

        RecyclerView rv = findViewById(R.id.recycler_view_reservations);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshGuestNotifications();
    }

    private void refreshGuestNotifications() {
        AppDatabaseHelper db = new AppDatabaseHelper(this);
        List<NotificationModel> notifications = db.getGuestNotifications(username);

        notificationAdapter.setNotifications(notifications);

        if (notifications == null || notifications.isEmpty()) {
            textNoNotifications.setVisibility(View.VISIBLE);
        } else {
            textNoNotifications.setVisibility(View.GONE);
        }
    }


    private void setupNavigationAndActions() {
        Button newReservationButton = findViewById(R.id.button_new_reservation);
        Button menuNavButton = findViewById(R.id.button_menu_nav);
        Button callNavButton = findViewById(R.id.button_call_nav);

        // new reservation nav
        newReservationButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReservationActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // menu list nav
        menuNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuListActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // contact screen nav
        callNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> logoutUser());
    }

    // placeholder method for getting the users reservations
    private void fetchAndDisplayReservations() {
        // where I connect to my database like appdatabasehelper

        // get reservation records and update list
        Toast.makeText(this, "reservations fetched and displayed", Toast.LENGTH_SHORT).show();
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