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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

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

        // add image form action
        addImageButton.setOnClickListener(v -> {
            // add code to pick from device gallery
        });

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

        // placeholder for save logic
        Toast.makeText(this, "Item saved, Name: " + name + ", Price: £" + price, Toast.LENGTH_LONG).show();

        // back to main menu after saving
        Intent intent = new Intent(this, ManageMenuActivity.class);
        startActivity(intent);
        finish();
    }
}