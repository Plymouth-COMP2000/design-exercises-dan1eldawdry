package com.example.restaurantguestapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    // UI elements
    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginButton;

    private static final String STUDENT_ID = "10911123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.editText_email);
        loginPassword = findViewById(R.id.editText_password);
        loginButton = findViewById(R.id.button_login_guest);

        // listener
        loginButton.setOnClickListener(v -> handleLoginAttempt());
    }

    // starts login process by calling UserService to get users
    private void handleLoginAttempt() {
        // grabs and validates the input
        String enteredUsername = loginUsername.getText().toString().trim();
        String enteredPassword = loginPassword.getText().toString().trim();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(this, "enter both username and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // just disable button to make UX cleaner
        loginButton.setEnabled(false);

        // Volley call
        UserService.readAllUsers(this, STUDENT_ID, new UserListCallback() {

            // success
            @Override
            public void onSuccess(List<User> userList) {
                loginButton.setEnabled(true);
                // moves to local authentication
                localAuthentication(enteredUsername, enteredPassword, userList);
            }

            // fail
            @Override
            public void onFailure(String errorMessage) {
                loginButton.setEnabled(true);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    // local cred check and role based access check
    private void localAuthentication(String username, String password, List<User> userList) {

        for (User user : userList) {
            // check creds match
            if (user.username.equals(username) && user.password.equals(password)) {

                // check role
                String userRole = user.usertype;

                if (userRole.equalsIgnoreCase("staff")) {
                    // staff route
                    Toast.makeText(this, "Staff Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, StaffDashboardActivity.class));
                } else {
                    // guest route
                    Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MenuListActivity.class));
                }

                finish();
                return;
            }
        }

        // if loop finishes without match
        Toast.makeText(this, "Login Failed: Invalid username or password.", Toast.LENGTH_LONG).show();
    }
}