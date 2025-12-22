package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class StaffLoginActivity extends AppCompatActivity {

    // input fields
    private EditText emailInput;
    private EditText passwordInput;

    private Button backButton;
    private Button loginButton;

    private static final String STUDENT_ID = "10911123"; // student id for API access

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        // link ui and java
        emailInput = findViewById(R.id.editText_staff_id);
        passwordInput = findViewById(R.id.editText_staff_password);
        backButton = findViewById(R.id.button_back_to_guest_login);
        loginButton = findViewById(R.id.button_staff_login_submit);

        // button listeners
        backButton.setOnClickListener(v -> {
            finish();
        });
        loginButton.setOnClickListener(v -> attemptLogin());
    }

    // starts login process by calling UserService to get users
    private void attemptLogin() {

        // grabs and validates the input
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "enter both email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // disalbed button to stop multiple taps
        loginButton.setEnabled(false);

        // get users from API with a volley call
        UserService.readAllUsers(this, STUDENT_ID, new UserListCallback() {

            // success
            @Override
            public void onSuccess(List<User> userList) {
                loginButton.setEnabled(true);
                authenticateStaff(email, password, userList);
            }

            // fail
            @Override
            public void onFailure(String errorMessage) {
                loginButton.setEnabled(true);
                Toast.makeText(StaffLoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    // local cred check and moves user on based on role for staff
    private void authenticateStaff(String email, String password, List<User> users) {

        for (User user : users) {
            // check creds match
            if (user.email.equals(email) && user.password.equals(password)) {

                String role = user.usertype;
                String username = user.username;

                // staff access
                if (role.equalsIgnoreCase("staff")) {

                    Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, StaffDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear back stack
                    startActivity(intent);
                    return;

                } else {
                    // if login work but guest
                    Toast.makeText(this, "you are a guest user move over to Guest Login", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        // if loop finishes without match
        Toast.makeText(this, "login failed: invalid username or password", Toast.LENGTH_LONG).show();
    }
}