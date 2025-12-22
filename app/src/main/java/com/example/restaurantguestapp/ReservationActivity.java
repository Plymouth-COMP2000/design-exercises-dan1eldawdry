package com.example.restaurantguestapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

public class ReservationActivity extends AppCompatActivity {

    // form
    private TextView dateText;
    private TextView timeText;
    private EditText groupSizeInput;
    private EditText requestsInput;
    private Button backButton;
    private Button selectDateButton;
    private Button confirmButton;

    private AppDatabaseHelper db; // database helper

    private int reservationId = -1; // checks if they are editing an existing reservation

    private String username; // stores current users username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        username = getIntent().getStringExtra("username"); // gets username that was passed through

        db = new AppDatabaseHelper(this);

        // link ui and java
        dateText = findViewById(R.id.selectedDateText);
        timeText = findViewById(R.id.selectedTimeText);
        groupSizeInput = findViewById(R.id.groupSizeInput);
        requestsInput = findViewById(R.id.requestsInput);
        backButton = findViewById(R.id.backButton);
        selectDateButton = findViewById(R.id.changeDateButton);
        confirmButton = findViewById(R.id.confirmBookingButton);

        // checks if its editing
        reservationId = getIntent().getIntExtra("reservation_id", -1);
        if (reservationId != -1) {
            loadReservation(); // fill fields for editing
        }

        setupButtons();
    }

    // click listeners for all buttons on screen
    private void setupButtons() {
        backButton.setOnClickListener(v -> finish()); // go back a screen
        selectDateButton.setOnClickListener(v -> openDatePicker()); // opens the date picker
        timeText.setOnClickListener(v -> openTimePicker()); // opens the time picker
        confirmButton.setOnClickListener(v -> saveReservation()); // always go through saveReservation()
    }

    // opens a date picker widget and updates the date
    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format(
                    Locale.getDefault(),
                    "%d/%d/%d",
                    dayOfMonth,
                    month + 1,
                    year
            );
            dateText.setText(date);
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // prevents selecting past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    // opens time picker selection
    private void openTimePicker() {
        final String[] timeOptions = {
                "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00",
                "18:00", "19:00", "20:00", "21:00",
                "22:00"
        };

        new AlertDialog.Builder(this)
                .setTitle("Select Time")
                .setItems(timeOptions, (dialog, which) -> {
                    timeText.setText(timeOptions[which]);
                })
                .show();
    }

    // loads data into reservation form when editing
    private void loadReservation() {
        ReservationModel reservation = db.getReservationById(reservationId);
        if (reservation == null) return;

        // fill fields
        dateText.setText(reservation.getDate());
        timeText.setText(reservation.getTime());
        groupSizeInput.setText(String.valueOf(reservation.getGroupSize()));
        requestsInput.setText(reservation.getSpecialRequests());
    }

    // checks the input and saves it
    private void saveReservation() {
        String date = dateText.getText().toString().trim();
        String time = timeText.getText().toString().trim();
        String groupSizeText = groupSizeInput.getText().toString().trim();
        String requests = requestsInput.getText().toString().trim();

        // input validation
        if (date.isEmpty() || date.equals("Select Date")) {
            Toast.makeText(this, "please pick a reservation date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (time.isEmpty() || time.equals("Select Time")) {
            Toast.makeText(this, "please pick a reservation time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (groupSizeText.isEmpty()) {
            Toast.makeText(this, "please put the group size", Toast.LENGTH_SHORT).show();
            return;
        }

        int groupSize;
        try {
            groupSize = Integer.parseInt(groupSizeText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "group size must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success;

        if (reservationId == -1) {
            // making a new reservation uses addReservation
            success = db.addReservation(
                    username,
                    date,
                    time,
                    groupSize,
                    requests
            );

            if (success) {
                String message = "new reservation made by " + username +
                        " for " + date + " at " + time;

                // stores notifictaions for staff
                db.addStaffNotification(message);

                // actual andoird notification
                NotificationHelper helper = new NotificationHelper(this);
                helper.send("New Reservation", message);
            }

        } else {
            // if editing existing reservation. then it uses updateReservation
            ReservationModel updatedReservation = new ReservationModel(
                    reservationId,
                    username,
                    date,
                    time,
                    groupSize,
                    requests
            );
            success = db.updateReservation(updatedReservation);
        }

        if (success) {
            Toast.makeText(this, "reservation saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "error saving reservation", Toast.LENGTH_SHORT).show();
        }
    }
}