package com.example.restaurantguestapp;

public final class TableNames {
    // menu items constants
    public static abstract class MenuItemEntry {
        public static final String TABLE_NAME = "menu_items";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";
    }

    // reservation table constant
    public static abstract class ReservationEntry {
        public static final String TABLE_NAME = "reservations";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_GROUP_SIZE = "group_size";
        public static final String COLUMN_NAME_STATUS = "status";
    }
}
