package com.example.restaurantguestapp;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuListActivity extends AppCompatActivity {

    private Button reservationsButton;
    private Button callButton;
    private Button logoutButton;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        username = getIntent().getStringExtra("username"); // gets the username that was passed through


        // find bottom nav buttons
        reservationsButton = findViewById(R.id.button_reservations);
        callButton = findViewById(R.id.button_call);
        logoutButton = findViewById(R.id.button_logout);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_menu_guest);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabaseHelper db = new AppDatabaseHelper(this);
        List<MenuItemModel> items = db.getAllMenuItems();

        MenuAdapter adapter = new MenuAdapter(this, items, false);
        recyclerView.setAdapter(adapter);


        setupButtonListeners();
    }

    private void setupButtonListeners() {
        // listner for reservations
        reservationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyReservationsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);

        });

        // listener for call
        callButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
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