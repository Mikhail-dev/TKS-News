package com.mikhaildev.tsknews.database;

import android.database.sqlite.SQLiteDatabase;


public class HeadlineTable {

    public static final String TABLE_NAME = "headline";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_PUBLICATION_DATE = "publication_date";
    public static final String COLUMN_BANK_INFO_TYPE_ID = "bank_info_type_id";

    public static final String DEFAULT_SORT_ORDER = COLUMN_PUBLICATION_DATE + " DESC";

    public static final String[] PROJECTION = new String[] {
            COLUMN_ID, COLUMN_NAME, COLUMN_TEXT, COLUMN_PUBLICATION_DATE, COLUMN_BANK_INFO_TYPE_ID
    };

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_TEXT + " text not null,"
            + COLUMN_PUBLICATION_DATE + " integer not null,"
            + COLUMN_BANK_INFO_TYPE_ID + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        //fast implementation of upgrade
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }


}
