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
    private AppDatabaseHelper db;
    private int editingReservationId = -1;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        username = getIntent().getStringExtra("username"); // gets username that was passed through

        db = new AppDatabaseHelper(this);

        initializeUI();

        // checks if its editing
        editingReservationId = getIntent().getIntExtra("reservation_id", -1);
        if (editingReservationId != -1) {
            loadReservation(); // fill fields for editing
        }

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

        // always go through saveReservation()
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
        final String[] timeOptions = {
                "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00",
                "18:00", "19:00", "20:00", "21:00",
                "22:00"
        };

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Select Time")
                .setItems(timeOptions, (dialog, which) -> {
                    selectedTimeText.setText(timeOptions[which]);
                })
                .show();
    }


    private void loadReservation() {
        ReservationModel r = db.getReservationById(editingReservationId);
        if (r == null) return;

        // fill fields
        selectedDateText.setText(r.getDate());
        selectedTimeText.setText(r.getTime());
        groupSizeEditText.setText(String.valueOf(r.getGroupSize()));
        specialRequestsEditText.setText(r.getSpecialRequests());
    }

    private void saveReservation() {
        String date = selectedDateText.getText().toString().trim();
        String time = selectedTimeText.getText().toString().trim();
        String groupSizeStr = groupSizeEditText.getText().toString().trim();
        String requests = specialRequestsEditText.getText().toString().trim();

        // simple validation
        if (date.equals("Select Date") || date.isEmpty()) {
            Toast.makeText(this, "Please select a reservation date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (time.equals("Select Time") || time.isEmpty()) {
            Toast.makeText(this, "Please select a reservation time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (groupSizeStr.isEmpty()) {
            Toast.makeText(this, "Please enter the group size.", Toast.LENGTH_SHORT).show();
            return;
        }

        int groupSize;
        try {
            groupSize = Integer.parseInt(groupSizeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Group size must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success;

        if (editingReservationId == -1) {
            // making a new reservation uses insertReservation
            success = db.insertReservation(
                    username,
                    date,
                    time,
                    groupSize,
                    requests
            );
        } else {
            // if editing existing reservation. then it uses editReservation
            ReservationModel model = new ReservationModel(
                    editingReservationId,
                    username,
                    date,
                    time,
                    groupSize,
                    requests
            );
            success = db.editReservation(model);
        }
        if (success) {
            Toast.makeText(this, "Reservation saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving reservation", Toast.LENGTH_SHORT).show();
        }
    }
}