package com.mikhaildev.tsknews.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;


public class TksNewsProvider extends ContentProvider {

    private DatabaseHelper database;

    private static final int HEADLINES = 10;
    private static final int HEADLINE_ID = 20;

    private static final String AUTHORITY = "com.mikhaildev.tksnews.TksNewsProvider";

    private static final String HEADLINE_PATH = "headline";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + HEADLINE_PATH);


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, HEADLINE_PATH, HEADLINES);
        sURIMatcher.addURI(AUTHORITY, HEADLINE_PATH + "/#", HEADLINE_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(HeadlineTable.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case HEADLINES:
                break;
            case HEADLINE_ID:
                queryBuilder.appendWhere(HeadlineTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        int updatedRowsCount = 0;
        try {
            for (ContentValues cv : values) {
                boolean success = db.insertWithOnConflict(HeadlineTable.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE) !=-1;
                if (success) ++updatedRowsCount;
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
        } finally {
            db.endTransaction();
        }
        return updatedRowsCount;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case HEADLINES:
                id = sqlDB.insertWithOnConflict(HeadlineTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(HEADLINE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case HEADLINES:
                rowsDeleted = sqlDB.delete(HeadlineTable.TABLE_NAME, selection, selectionArgs);
                break;
            case HEADLINE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(HeadlineTable.TABLE_NAME, HeadlineTable.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(HeadlineTable.TABLE_NAME,
                            HeadlineTable.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case HEADLINES:
                rowsUpdated = sqlDB.update(HeadlineTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case HEADLINE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(HeadlineTable.TABLE_NAME,
                            values, HeadlineTable.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(HeadlineTable.TABLE_NAME, values,
                            HeadlineTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { HeadlineTable.COLUMN_ID, HeadlineTable.COLUMN_NAME, HeadlineTable.COLUMN_TEXT,
                HeadlineTable.COLUMN_PUBLICATION_DATE, HeadlineTable.COLUMN_BANK_INFO_TYPE_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}
