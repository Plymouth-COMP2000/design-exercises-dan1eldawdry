package com.example.restaurantguestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class StaffLoginActivity extends AppCompatActivity {

    private EditText staffLoginEmail;
    private EditText staffLoginPassword;
    private Button backButton;
    private Button loginButton;

    // using my student ID to fetch users
    private static final String STUDENT_ID = "10911123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        staffLoginEmail = findViewById(R.id.editText_staff_id);
        staffLoginPassword = findViewById(R.id.editText_staff_password);
        backButton = findViewById(R.id.button_back_to_guest_login);
        loginButton = findViewById(R.id.button_staff_login_submit);

        backButton.setOnClickListener(v -> {
            finish();
        });

        loginButton.setOnClickListener(v -> handleLoginAttempt());
    }

    // staff login part
    private void handleLoginAttempt() {
        String enteredEmail = staffLoginEmail.getText().toString().trim();
        String enteredPassword = staffLoginPassword.getText().toString().trim();

        if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(this, "Enter both email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // disable button for better UX
        loginButton.setEnabled(false);

        // volley call
        UserService.readAllUsers(this, STUDENT_ID, new UserListCallback() {

            // local auth
            @Override
            public void onSuccess(List<User> userList) {
                loginButton.setEnabled(true);
                localAuthentication(enteredEmail, enteredPassword, userList);
            }

            // failure
            @Override
            public void onFailure(String errorMessage) {
                loginButton.setEnabled(true);
                Toast.makeText(StaffLoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void localAuthentication(String email, String password, List<User> userList) {

        for (User user : userList) {
            // cehck if creds match
            if (user.email.equals(email) && user.password.equals(password)) {

                // check role
                String userRole = user.usertype;
                String username = user.username;

                if (userRole.equalsIgnoreCase("staff")) {
                    Toast.makeText(this, "Welcome Staff " + username, Toast.LENGTH_SHORT).show();
                    Intent staffDashboardIntent = new Intent(this, StaffDashboardActivity.class);
                    staffDashboardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(staffDashboardIntent);
                    return;
                } else {
                    // if login user is a guest
                    Toast.makeText(this, "You are a Guest user. Move over to Guest Login.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        // loop finishes without a match
        Toast.makeText(this, "Staff Login Failed: Invalid email or password.", Toast.LENGTH_LONG).show();
    }
}