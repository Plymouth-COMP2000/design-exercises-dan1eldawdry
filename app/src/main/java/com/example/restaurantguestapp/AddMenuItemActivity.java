package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddMenuItemActivity extends AppCompatActivity {

    private Button backButton; // navigation back

    // form
    private EditText nameInput;
    private EditText priceInput;
    private EditText descriptionInput;
    private Button saveButton;

    private boolean isEditing = false; // used to see if staff is editing or adding an item
    private int itemId = -1; // stores the id of the item when editing


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        // link ui and java
        backButton = findViewById(R.id.backButton);
        nameInput = findViewById(R.id.itemNameInput);
        priceInput = findViewById(R.id.itemPriceInput);
        descriptionInput = findViewById(R.id.itemDescriptionInput);
        saveButton = findViewById(R.id.saveItemButton);

        checkForEditMode();

        setupButtons();
    }

    // checks the intent to see if an item id was passed in.
    // if it was then it switches to edit mode
    private void checkForEditMode() {
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("menu_item_id")) {
            itemId = intent.getIntExtra("menu_item_id", -1);

            if (itemId != -1) {
                isEditing = true;
                loadMenuItem(itemId);
                saveButton.setText("Update Menu Item");
            }
        }
    }

    // click listeners for all buttons on screen
    private void setupButtons() {
        backButton.setOnClickListener(v -> finish()); // go back a screen
        saveButton.setOnClickListener(v -> saveMenuItem()); // save or update menu item
    }

    // saves a new menu item or update one already there
    private void saveMenuItem() {
        String name = nameInput.getText().toString().trim();
        String price = priceInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        // makes sure all the fields are filled in
        if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "please fill in all details.", Toast.LENGTH_LONG).show();
            return;
        }

        //database helper that interacts with database
        AppDatabaseHelper db = new AppDatabaseHelper(this);

        if (isEditing) {
            // update exisitng item
            db.updateMenuItem(itemId, name, price, description);
            Toast.makeText(this, "menu item updated", Toast.LENGTH_SHORT).show();
        } else {
            // add new item
            db.addMenuItem(name, price, description);
            Toast.makeText(this, "menu item added", Toast.LENGTH_SHORT).show();
        }

        // go back to menu manage screen
        startActivity(new Intent(this, ManageMenuActivity.class));
        finish();
    }

    // loads an existing items data into the input fields
    private void loadMenuItem(int id) {
        AppDatabaseHelper db = new AppDatabaseHelper(this);
        MenuItemModel menuItem = db.getMenuItemById(id);

        if (menuItem != null) {
            nameInput.setText(menuItem.getName());
            priceInput.setText(String.valueOf(menuItem.getPrice()));
            descriptionInput.setText(menuItem.getDescription());
        }
    }
}