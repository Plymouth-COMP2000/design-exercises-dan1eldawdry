package com.example.restaurantguestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

        // menu table
        String createMenuTable = "CREATE TABLE menu_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "description TEXT," +
                "price REAL" +
                ");";

        db.execSQL(createReservationsTable);
        db.execSQL(createMenuTable);
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

    // edit a reservation
    public boolean editReservation(ReservationModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("date", model.getDate());
        values.put("time", model.getTime());
        values.put("group_size", model.getGroupSize());
        values.put("special_requests", model.getSpecialRequests());

        int rows = db.update("reservations", values, "id=?", new String[]{String.valueOf(model.getId())});
        return rows > 0;
    }

    // delete a reservation
    public boolean deleteReservation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("reservations", "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // get a single reservation by its id
    public ReservationModel getReservationById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM reservations WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        ReservationModel r = null;

        if (cursor.moveToFirst()) {
            r = new ReservationModel(
                    cursor.getInt(0),   // id
                    cursor.getString(1),// username
                    cursor.getString(2),// date
                    cursor.getString(3),// time
                    cursor.getInt(4),   // group size
                    cursor.getString(5),// special requests
                    cursor.getString(6) // status
            );
        }

        cursor.close();
        return r;
    }

    // so i can update staff dashboard reservation counter
    public int getReservationCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM reservations",
                null
        );

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    public void addMenuItem(String name, String description, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        db.insert("menu_items", null, values);
    }

    public void updateMenuItem(int id, String name, String description, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        db.update("menu_items", values, "id=?", new String[]{String.valueOf(id)});
    }

    public void deleteMenuItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("menu_items", "id=?", new String[]{String.valueOf(id)});
    }

    public List<MenuItemModel> getAllMenuItems() {
        List<MenuItemModel> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM menu_items", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                items.add(new MenuItemModel(id, name, price, description));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return items;
    }


    public MenuItemModel getMenuItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM menu_items WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        MenuItemModel item = null;

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

            item = new MenuItemModel(id, name, price, description);
        }

        cursor.close();
        return item;
    }
}

