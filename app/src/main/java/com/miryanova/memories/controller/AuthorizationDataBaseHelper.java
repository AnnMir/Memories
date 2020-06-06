package com.miryanova.memories.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miryanova.memories.model.AuthorizationContract.AuthorizationEntry;

public class AuthorizationDataBaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Authorization.db";

    AuthorizationDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + AuthorizationEntry.TABLE_NAME + " (" +
                AuthorizationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AuthorizationEntry.LOGIN + " TEXT NOT NULL," +
                AuthorizationEntry.PASSWORD + " TEXT NOT NULL," +
                AuthorizationEntry.EMAIL + " TEXT NOT NULL)";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + AuthorizationEntry.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
