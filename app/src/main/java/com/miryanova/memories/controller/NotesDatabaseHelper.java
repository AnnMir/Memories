package com.miryanova.memories.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miryanova.memories.model.NotesContract.NotesEntry;

public class NotesDatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Notes.db";

    NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + NotesEntry.TABLE_NAME + " (" +
                NotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NotesEntry.UUID + " TEXT NOT NULL," +
                NotesEntry.LOGIN + " TEXT NOT NULL," +
                NotesEntry.DATE + " DATE NOT NULL," +
                NotesEntry.TITLE + " TEXT," +
                NotesEntry.CONTENT + " TEXT," +
                NotesEntry.RESOURCES + " TEXT)";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + NotesEntry.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
