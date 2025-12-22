package com.example.restaurantguestapp;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuListActivity extends AppCompatActivity {

    // navigation buttons
    private Button reservationsButton;
    private Button callButton;
    private Button logoutButton;

    private String username; // stores current users username


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        username = getIntent().getStringExtra("username"); // gets the username that was passed through

        // link ui and java
        reservationsButton = findViewById(R.id.reservationsButton);
        callButton = findViewById(R.id.callButton);
        logoutButton = findViewById(R.id.logoutButton);

        setupRecyclerView();

        setupButtons();
    }

    // sets up the recyclerview that displays menu items
    private void setupRecyclerView() {

        RecyclerView menuRecyclerView = findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabaseHelper db = new AppDatabaseHelper(this);
        List<MenuItemModel> menuItems = db.getAllMenuItems();

        // false hides staff edit buttons
        MenuAdapter adapter = new MenuAdapter(this, menuItems, false);
        menuRecyclerView.setAdapter(adapter);
    }

    // click listeners for all buttons on screen
    private void setupButtons() {

        // opens reservations screen
        reservationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyReservationsActivity.class);
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