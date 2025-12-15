package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddMenuItemActivity extends AppCompatActivity {

    private Button backButton;

    // form
    private EditText itemNameEditText;
    private EditText itemPriceEditText;
    private EditText itemDescriptionEditText;
    private Button addImageButton;
    private Button saveItemButton;
    private boolean isEditMode = false;
    private int menuItemId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        Intent intent = getIntent();
        if (intent.hasExtra("menu_item_id")) {
            isEditMode = true;
            menuItemId = intent.getIntExtra("menu_item_id", -1);

            AppDatabaseHelper db = new AppDatabaseHelper(this);
            MenuItemModel item = db.getMenuItemById(menuItemId);

            if (item != null) {
                itemNameEditText.setText(item.getName());
                itemPriceEditText.setText(item.getPrice());
                itemDescriptionEditText.setText(item.getDescription());
            }

            saveItemButton.setText("UPDATE MENU ITEM");
        }


        backButton = findViewById(R.id.button_back);
        itemNameEditText = findViewById(R.id.edit_text_item_name);
        itemPriceEditText = findViewById(R.id.edit_text_item_price);
        itemDescriptionEditText = findViewById(R.id.edit_text_item_description);
        addImageButton = findViewById(R.id.button_add_image);
        saveItemButton = findViewById(R.id.button_save_item);

        setupButtonListeners();
    }

    private void setupButtonListeners() {

        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                finish();
            });
        }

        saveItemButton.setOnClickListener(v -> saveMenuItem());
    }

    private void saveMenuItem() {

        String name = itemNameEditText.getText().toString().trim();
        String price = itemPriceEditText.getText().toString().trim();
        String description = itemDescriptionEditText.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all item details.", Toast.LENGTH_LONG).show();
            return;
        }

        AppDatabaseHelper db = new AppDatabaseHelper(this);

        if (isEditMode) {
            db.updateMenuItem(menuItemId, name, price, description);
            Toast.makeText(this, "Menu item updated", Toast.LENGTH_SHORT).show();
        } else {
            db.addMenuItem(name, price, description);
            Toast.makeText(this, "Menu item added", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

}