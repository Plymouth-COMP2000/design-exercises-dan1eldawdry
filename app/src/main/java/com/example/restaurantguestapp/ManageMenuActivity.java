package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManageMenuActivity extends AppCompatActivity {

    // navigation buttons
    private Button logoutButton;
    private Button addItemButton;
    private Button dashboardButton;
    private Button manageMenuButton;
    private Button viewReservationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        // link ui and java
        logoutButton = findViewById(R.id.logoutButton);
        addItemButton = findViewById(R.id.addMenuItemButton);
        dashboardButton = findViewById(R.id.dashboardButton);
        viewReservationsButton = findViewById(R.id.viewReservationsButton);

        setupRecyclerView();

        setupButtons();
    }

    // sets up the recyclerview that displays menu items
    private void setupRecyclerView(){
        RecyclerView menuRecyclerView = findViewById(R.id.menuRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabaseHelper db = new AppDatabaseHelper(this);
        List<MenuItemModel> menuItems = db.getAllMenuItems();

        MenuAdapter adapter = new MenuAdapter(this, menuItems, true); // true allows staff editing in the adapter
        menuRecyclerView.setAdapter(adapter);
    }

    // click listeners for all buttons on screen
    private void setupButtons() {

        // opens add new menu item screen
        addItemButton.setOnClickListener(v ->
                startActivity(new Intent(this, AddMenuItemActivity.class))
        );

        //opens staff dashboard screen
        dashboardButton.setOnClickListener(v -> {
            startActivity(new Intent(this, StaffDashboardActivity.class));
            finish();
        });

        // opens view reservations screen
        viewReservationsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewReservationsActivity.class));
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