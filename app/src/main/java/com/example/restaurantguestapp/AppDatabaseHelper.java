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

    // Reservations table
    public static final String RESERVATION_TABLE = "reservations";
    public static final String RESERVATION_ID = "id";
    public static final String RESERVATION_USERNAME = "username";
    public static final String RESERVATION_DATE = "date";
    public static final String RESERVATION_TIME = "time";
    public static final String RESERVATION_GROUP_SIZE = "group_size";
    public static final String RESERVATION_SPECIAL_REQUESTS = "special_requests";

    // Menu item table
    public static final String MENU_TABLE = "menu_items";
    public static final String MENU_ID = "id";
    public static final String MENU_NAME = "name";
    public static final String MENU_DESCRIPTION = "description";
    public static final String MENU_PRICE = "price";

    // Notifications table
    public static final String NOTIFICATION_TABLE = "guest_notifications";
    public static final String NOTIFICATION_ID = "id";
    public static final String NOTIFICATION_USERNAME = "username";
    public static final String NOTIFICATION_MESSAGE = "message";
    public static final String NOTIFICATION_CREATED_AT = "created_at";

    // constructor to create database helper
    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // lifecycle methods
    // creates all needed tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // reservations table
        String createReservationsTable =
                "CREATE TABLE " + RESERVATION_TABLE + " (" +
                        RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RESERVATION_USERNAME + " TEXT, " +
                        RESERVATION_DATE + " TEXT, " +
                        RESERVATION_TIME + " TEXT, " +
                        RESERVATION_GROUP_SIZE + " INTEGER, " +
                        RESERVATION_SPECIAL_REQUESTS + " TEXT" +
                        ");";

        // menu table
        String createMenuTable =
                "CREATE TABLE " + MENU_TABLE + " (" +
                        MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MENU_NAME + " TEXT, " +
                        MENU_DESCRIPTION + " TEXT, " +
                        MENU_PRICE + " REAL" +
                        ");";

        // notification table
        String createNotificationsTable =
                "CREATE TABLE " + NOTIFICATION_TABLE + " (" +
                        NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NOTIFICATION_USERNAME + " TEXT, " +
                        NOTIFICATION_MESSAGE + " TEXT, " +
                        NOTIFICATION_CREATED_AT + " INTEGER" +
                        ");";

        db.execSQL(createReservationsTable);
        db.execSQL(createMenuTable);
        db.execSQL(createNotificationsTable);
    }

    // when database version changes. this drops all tables and recreates them
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RESERVATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MENU_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATION_TABLE);
        onCreate(db);
    }

    // RESERVATION METHODS BELOW

    // inserts new reservation
    public boolean addReservation(String username, String date, String time, int groupSize, String specialRequests) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RESERVATION_USERNAME, username);
        values.put(RESERVATION_DATE, date);
        values.put(RESERVATION_TIME, time);
        values.put(RESERVATION_GROUP_SIZE, groupSize);
        values.put(RESERVATION_SPECIAL_REQUESTS, specialRequests);

        return db.insert(RESERVATION_TABLE, null, values) != -1;
    }

    // returns reservations for the logged in user
    public ArrayList<ReservationModel> getReservationsForUser(String username) {

        ArrayList<ReservationModel> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                RESERVATION_TABLE,
                null,
                RESERVATION_USERNAME + "=?",
                new String[]{username},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                reservations.add(new ReservationModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(RESERVATION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RESERVATION_GROUP_SIZE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_SPECIAL_REQUESTS))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return reservations;
    }

    // returns all reservations for staff
    public ArrayList<ReservationModel> getAllReservations() {

        ArrayList<ReservationModel> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                RESERVATION_TABLE,
                null,
                null,
                null,
                null,
                null,
                RESERVATION_DATE + ", " + RESERVATION_TIME
        );

        if (cursor.moveToFirst()) {
            do {
                reservations.add(new ReservationModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(RESERVATION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RESERVATION_GROUP_SIZE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_SPECIAL_REQUESTS))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return reservations;
    }

    // update an exisitng reservation
    public boolean updateReservation(ReservationModel reservation) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RESERVATION_DATE, reservation.getDate());
        values.put(RESERVATION_TIME, reservation.getTime());
        values.put(RESERVATION_GROUP_SIZE, reservation.getGroupSize());
        values.put(RESERVATION_SPECIAL_REQUESTS, reservation.getSpecialRequests());

        return db.update(
                RESERVATION_TABLE,
                values,
                RESERVATION_ID + "=?",
                new String[]{String.valueOf(reservation.getId())}
        ) > 0;
    }

    // delete a reservation by its id
    public boolean deleteReservation(int reservationId) {

        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                RESERVATION_TABLE,
                RESERVATION_ID + "=?",
                new String[]{String.valueOf(reservationId)}
        ) > 0;
    }

    // get a single reservation by its id
    public ReservationModel getReservationById(int reservationId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ReservationModel reservation = null;

        Cursor cursor = db.query(
                RESERVATION_TABLE,
                null,
                RESERVATION_ID + "=?",
                new String[]{String.valueOf(reservationId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            reservation = new ReservationModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(RESERVATION_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_TIME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(RESERVATION_GROUP_SIZE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(RESERVATION_SPECIAL_REQUESTS))
            );
        }

        cursor.close();
        return reservation;
    }

    // returns total number of reservations for staff dashboard
    public int getReservationCount() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + RESERVATION_TABLE,
                null
        );

        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);

        cursor.close();
        return count;
    }

    // MENU ITEM METHODS BELOW

    // adds a new menu item
    public void addMenuItem(String name, String description, String price) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MENU_NAME, name);
        values.put(MENU_DESCRIPTION, description);
        values.put(MENU_PRICE, price);

        db.insert("menu_items", null, values);
    }

    // updates an exisitng menu item
    public void updateMenuItem(int itemId, String name, String description, String price) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MENU_NAME, name);
        values.put(MENU_DESCRIPTION, description);
        values.put(MENU_PRICE, price);

        db.update(
                MENU_TABLE,
                values,
                MENU_ID + "=?",
                new String[]{String.valueOf(itemId)}
        );
    }

    // delete a menu item by its id
    public void deleteMenuItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MENU_TABLE, MENU_ID + "=?", new String[]{String.valueOf(itemId)});
    }

    // returns all menu items
    public List<MenuItemModel> getAllMenuItems() {

        List<MenuItemModel> menuItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                MENU_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                menuItems.add(new MenuItemModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(MENU_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MENU_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MENU_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MENU_DESCRIPTION))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return menuItems;
    }

    // returns one menu item by its id
    public MenuItemModel getMenuItemById(int itemId) {

        SQLiteDatabase db = this.getReadableDatabase();
        MenuItemModel menuItem = null;

        Cursor cursor = db.query(
                MENU_TABLE,
                null,
                MENU_ID + "=?",
                new String[]{String.valueOf(itemId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            menuItem = new MenuItemModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(MENU_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MENU_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MENU_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MENU_DESCRIPTION))
            );
        }

        cursor.close();
        return menuItem;
    }

    // returns number of menu items for staff dashboard
    public int getMenuItemCount() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + MENU_TABLE,
                null
        );

        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);

        cursor.close();
        return count;
    }

    // NOTIFICATION METHODS BELOW

    // adds notification for guest
    public void addGuestNotification(String username, String message) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOTIFICATION_USERNAME, username);
        values.put(NOTIFICATION_MESSAGE, message);
        values.put(NOTIFICATION_CREATED_AT, System.currentTimeMillis());

        db.insert(NOTIFICATION_TABLE, null, values);
        db.close();
    }

    // returns all notifications for guest
    public List<NotificationModel> getGuestNotifications(String username) {

        List<NotificationModel> notifications = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                NOTIFICATION_TABLE,
                null,
                NOTIFICATION_USERNAME + "=?",
                new String[]{username},
                null,
                null,
                NOTIFICATION_CREATED_AT + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                notifications.add(new NotificationModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(NOTIFICATION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NOTIFICATION_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NOTIFICATION_MESSAGE)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(NOTIFICATION_CREATED_AT))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return notifications;
    }

    // add notification visible to staff only
    public void addStaffNotification(String message) {
        addGuestNotification("STAFF", message);
    }

    // returns all staff notifications
    public List<NotificationModel> getStaffNotifications() {
        return getGuestNotifications("STAFF");
    }
}

