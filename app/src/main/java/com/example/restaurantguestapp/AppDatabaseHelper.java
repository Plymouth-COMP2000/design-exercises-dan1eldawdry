package com.example.restaurantguestapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabaseHelper extends SQLiteOpenHelper {
    // database details
    private static final String DATABASE_NAME = "restaurant.db";
    private static final int DATABASE_VERSION = 1;

    // singleton implemantation
    private static AppDatabaseHelper instance;

    // private constructor
    private AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // public method to get instance of database helper
    public static synchronized AppDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    // SQL to create tables
    private static final String SQL_CREATE_MENU_ITEMS = "CREATE TABLE " + TableNames.MenuItemEntry.TABLE_NAME + " (" + TableNames.MenuItemEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," + TableNames.MenuItemEntry.COLUMN_NAME_NAME + " TEXT," + TableNames.MenuItemEntry.COLUMN_NAME_PRICE + " REAL," + TableNames.MenuItemEntry.COLUMN_NAME_IMAGE_URL + " TEXT)";
    private static final String SQL_CREATE_RESERVATIONS = "CREATE TABLE " + TableNames.ReservationEntry.TABLE_NAME + " (" + TableNames.ReservationEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," + TableNames.ReservationEntry.COLUMN_NAME_USER_ID + " INTEGER," + TableNames.ReservationEntry.COLUMN_NAME_DATE + " TEXT," + TableNames.ReservationEntry.COLUMN_NAME_TIME + " TEXT," + TableNames.ReservationEntry.COLUMN_NAME_GROUP_SIZE + " INTEGER," + TableNames.ReservationEntry.COLUMN_NAME_STATUS + " TEXT)";

    // lifecycle methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MENU_ITEMS);
        db.execSQL(SQL_CREATE_RESERVATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //deletion and recreation
        db.execSQL("DROP TABLE IF EXISTS " + TableNames.MenuItemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableNames.ReservationEntry.TABLE_NAME);
        onCreate(db);
    }

    // Menu CRUD methods
    public long insertMenuItem(...) { return -1; }
    public android.database.Cursor getAllMenuItems() {return null; }

    // Reservation CRUD methods
    public long insertReservation(...) { return -1; }
    public android.database.Cursor getAllReservations() { return null; }
}
