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
    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private Button staffLoginButton;

    private static final String STUDENT_ID = "10911123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.editText_email);
        loginPassword = findViewById(R.id.editText_password);
        loginButton = findViewById(R.id.button_login_guest);
        staffLoginButton = findViewById(R.id.button_staff_login);

        // listener
        loginButton.setOnClickListener(v -> handleLoginAttempt());
        staffLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, StaffLoginActivity.class));
        });
    }

    // starts login process by calling UserService to get users
    private void handleLoginAttempt() {
        // grabs and validates the input
        String enteredEmail = loginEmail.getText().toString().trim();
        String enteredPassword = loginPassword.getText().toString().trim();

        if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(this, "enter both email and password.", Toast.LENGTH_SHORT).show();
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
                localAuthentication(enteredEmail, enteredPassword, userList);
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
    private void localAuthentication(String email, String password, List<User> userList) {

        for (User user : userList) {
            // check creds match
            if (user.email.equals(email) && user.password.equals(password)) {

                // check role
                String userRole = user.usertype;
                String username = user.username; // for welcocme test

                if (userRole.equalsIgnoreCase("guest")) {
                    // guest route
                    Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MenuListActivity.class);
                    intent.putExtra("username", username); // passes the username through
                    startActivity(intent);
                    finish();

                } else {
                    // if login work but staff
                    Toast.makeText(this, "You are a staff user. Move over to Staff Login", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        // if loop finishes without match
        Toast.makeText(this, "Login Failed: Invalid username or password.", Toast.LENGTH_LONG).show();
    }
}