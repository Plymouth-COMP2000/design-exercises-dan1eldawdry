package com.example.restaurantguestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AppDatabaseHelper extends SQLiteOpenHelper {
    // database details
    private static final String DATABASE_NAME = "restaurant.db";
    private static final int DATABASE_VERSION = 1;

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // lifecycle methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        // reservations table
        String createReservationsTable = "CREATE TABLE reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "date TEXT, " +
                "time TEXT, " +
                "group_size INTEGER, " +
                "special_requests TEXT, " +
                "status TEXT" +
                ");";

        db.execSQL(createReservationsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //deletion and recreation
        db.execSQL("DROP TABLE IF EXISTS reservations");
        onCreate(db);
    }

    // add new reservation
    public boolean insertReservation(String username, String date, String time, int groupSize,
                                     String specialRequests, String status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("username", username);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("group_size", groupSize);
        cv.put("special_requests", specialRequests);
        cv.put("status", status);

        long result = db.insert("reservations", null, cv);
        return result != -1; // true if success
    }

    // get users reservastions (for guest)
    public ArrayList<ReservationModel> getReservationsForUser(String username) {
        ArrayList<ReservationModel> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM reservations WHERE username=?",
                new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                ReservationModel r = new ReservationModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                list.add(r);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    // get all reservations (for staff)
    public ArrayList<ReservationModel> getAllReservations() {
        ArrayList<ReservationModel> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM reservations ORDER BY date, time", null);

        if (cursor.moveToFirst()) {
            do {
                ReservationModel r = new ReservationModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                list.add(r);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
}

