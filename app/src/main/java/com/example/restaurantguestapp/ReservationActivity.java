package com.example.restaurantguestapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivity extends AppCompatActivity {

    // UI Components
    private TextView selectedDateText;
    private TextView selectedTimeText;
    private EditText groupSizeEditText;
    private EditText specialRequestsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        initializeUI();
        setupReservationFormActions();
    }

    private void initializeUI() {
        selectedDateText = findViewById(R.id.text_selected_date);
        selectedTimeText = findViewById(R.id.text_selected_time);
        groupSizeEditText = findViewById(R.id.editText_group_size);
        specialRequestsEditText = findViewById(R.id.editText_special_requests);
    }

    private void setupReservationFormActions() {
        Button backButton = findViewById(R.id.button_back);
        Button selectDateButton = findViewById(R.id.button_select_date);
        Button confirmBookingButton = findViewById(R.id.button_confirm_booking);

        backButton.setOnClickListener(v -> finish());

        selectDateButton.setOnClickListener(v -> openDatePicker());

        selectedTimeText.setOnClickListener(v -> openTimePicker());

        confirmBookingButton.setOnClickListener(v -> saveReservation());
    }

    private void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // update textview with selected date
                    String date = String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, monthOfYear + 1, year1);
                    selectedDateText.setText(date);
                }, year, month, day);

        // restrict selection to today or in future
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void openTimePicker() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    // time format
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
                    selectedTimeText.setText(time);
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private void saveReservation() {
        String date = selectedDateText.getText().toString();
        String time = selectedTimeText.getText().toString();
        String groupSizeStr = groupSizeEditText.getText().toString().trim();
        String requests = specialRequestsEditText.getText().toString().trim();

        // validation
        if (date.equals("Select Date")) {
            Toast.makeText(this, "Please select a reservation date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (time.equals("Select Time")) {
            Toast.makeText(this, "Please select a reservation time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (groupSizeStr.isEmpty()) {
            Toast.makeText(this, "Please enter the group size.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int groupSize = Integer.parseInt(groupSizeStr);

            // logic to prevent booking 0 people or very large groups
            if (groupSize <= 0 || groupSize > 20) {
                Toast.makeText(this, "Group size must be between 1 and 20.", Toast.LENGTH_SHORT).show();
                return;
            }

            // database point

            // call appdatabasehelper here
            // AppDatabaseHelper dbHelper = AppDatabaseHelper.getInstance(this);
            // dbHelper.insertReservation(date, time, groupSize, requests);


            // placeholder message
            String confirmation = "Booking Confirmed for " + groupSize + " guests on " + date + " at " + time;
            Toast.makeText(this, confirmation, Toast.LENGTH_LONG).show();

            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid group size entered.", Toast.LENGTH_SHORT).show();
        }
    }
}