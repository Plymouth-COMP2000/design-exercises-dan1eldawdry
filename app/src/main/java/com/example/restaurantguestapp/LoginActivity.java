package com.example.restaurantguestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    // input fields
    private EditText emailInput;
    private EditText passwordInput;

    private Button loginButton;
    private Button staffLoginButton;

    private static final String STUDENT_ID = "10911123"; // student id for API access

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestNotificationPermission();

        // link ui and java
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        staffLoginButton = findViewById(R.id.staffLoginButton);

        // button listeners
        loginButton.setOnClickListener(v -> attemptLogin());
        staffLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, StaffLoginActivity.class));
        });
    }

    // asks permission to send notification on this first screen
    private void requestNotificationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }
    }

    // starts login process by calling UserService to get users
    private void attemptLogin() {

        // grabs and validates the input
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "enter both email and password", Toast.LENGTH_SHORT).show();
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
                authenticateUser(email, password, userList);
            }

            // fail
            @Override
            public void onFailure(String errorMessage) {
                loginButton.setEnabled(true);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    // local cred check and moves user on based on role
    private void authenticateUser(String email, String password, List<User> users) {

        for (User user : users) {
            // check creds match
            if (user.email.equals(email) && user.password.equals(password)) {

                String role = user.usertype;
                String username = user.username;

                // guest access
                if (role.equalsIgnoreCase("guest")) {

                    Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MenuListActivity.class);
                    intent.putExtra("username", username); // passes the username through
                    startActivity(intent);
                    finish();

                } else {
                    // if login work but staff
                    Toast.makeText(this, "you are a staff user move over to Staff Login", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        // if loop finishes without match
        Toast.makeText(this, "login failed: invalid email or password", Toast.LENGTH_LONG).show();
    }
}