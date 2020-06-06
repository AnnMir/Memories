package com.miryanova.memories.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.miryanova.memories.model.Note;
import com.miryanova.memories.model.NotesContract.NotesEntry;

import java.util.ArrayList;

public class NotesController {
    private NotesDatabaseHelper dataBaseHelper;
    private final static String LOG_TAG = "NotesController";

    public NotesController(Context context) {
        dataBaseHelper = new NotesDatabaseHelper(context);
    }

    public ArrayList<Note> selectUserNotes(String login) {
        ArrayList<Note> arrayList = new ArrayList<>();
        Note note;
        String selection;
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        String[] projection = {
                NotesEntry.UUID, NotesEntry.DATE, NotesEntry.TITLE, NotesEntry.CONTENT, NotesEntry.RESOURCES};
        String[] selectionArgs;
        selection = NotesEntry.LOGIN + "=?";
        selectionArgs = new String[]{login};
        Cursor cursor = db.query(
                NotesEntry.TABLE_NAME, // таблица
                projection,            // столбцы
                selection,             // столбцы для условия WHERE
                selectionArgs,         // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                NotesEntry.DATE + " ASC");  // порядок сортировки
        try {
            while (cursor.moveToNext()) {
                String currentUUID = cursor.getString(cursor.getColumnIndex(NotesEntry.UUID));
                String currentDate = cursor.getString(cursor.getColumnIndex(NotesEntry.DATE));
                String currentTitle = cursor.getString(cursor.getColumnIndex(NotesEntry.TITLE));
                String currentContent = cursor.getString(cursor.getColumnIndex(NotesEntry.CONTENT));
                String currentResources = cursor.getString(cursor.getColumnIndex(NotesEntry.RESOURCES));
                note = new Note(currentTitle, currentContent, currentDate, currentResources, currentUUID);
                arrayList.add(note);
            }
        } finally {
            cursor.close();
        }
        return arrayList;
    }

    public Note selectNote(String uuid){
        Note note = new Note(uuid);
        String selection;
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        String[] projection = {
                NotesEntry.DATE, NotesEntry.TITLE, NotesEntry.CONTENT, NotesEntry.RESOURCES};
        String[] selectionArgs;
        selection = NotesEntry.UUID + "=?";
        selectionArgs = new String[]{uuid};
        Cursor cursor = db.query(
                NotesEntry.TABLE_NAME, // таблица
                projection,            // столбцы
                selection,             // столбцы для условия WHERE
                selectionArgs,         // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                NotesEntry.DATE + " ASC");  // порядок сортировки
        try {
            while (cursor.moveToNext()) {
                String currentDate = cursor.getString(cursor.getColumnIndex(NotesEntry.DATE));
                String currentTitle = cursor.getString(cursor.getColumnIndex(NotesEntry.TITLE));
                String currentContent = cursor.getString(cursor.getColumnIndex(NotesEntry.CONTENT));
                String currentResources = cursor.getString(cursor.getColumnIndex(NotesEntry.RESOURCES));
                note = new Note(currentTitle, currentContent, currentDate, currentResources, uuid);
            }
        } finally {
            cursor.close();
        }
        return note;
    }

    /**
     * Функция добавления записки пользователя в базу данных
     *
     * @param uuid
     * @param login
     * @param title
     * @param content
     * @param resources
     */

    public void addNote(String uuid, String login, String title, String content, String date, String resources) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        //String currentDay = sdf.format(Calendar.getInstance().getTime());

        ContentValues values = new ContentValues();
        values.put(NotesEntry.UUID, uuid);
        values.put(NotesEntry.LOGIN, login);
        values.put(NotesEntry.DATE, date);
        values.put(NotesEntry.TITLE, title);
        values.put(NotesEntry.CONTENT, content);
        values.put(NotesEntry.RESOURCES, resources);

        db.insert(NotesEntry.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "added");
    }

    public void updateNote(String uuid, String login, String title, String content, String date, String resources){
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotesEntry.UUID, uuid);
        values.put(NotesEntry.LOGIN, login);
        values.put(NotesEntry.DATE, date);
        values.put(NotesEntry.TITLE, title);
        values.put(NotesEntry.CONTENT, content);
        values.put(NotesEntry.RESOURCES, resources);
        db.update(NotesEntry.TABLE_NAME,
                values,
                NotesEntry.UUID + " = ? AND " + NotesEntry.LOGIN + " = ?", new String[]{uuid,login});
    }

    /**
     * Функция удаления записки пользователя из базы данных
     *
     * @param uuid
     */

    public void deleteNote(String uuid) {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        db.delete(NotesEntry.TABLE_NAME, "UUID = ?", new String[]{uuid});
        Log.d(LOG_TAG, "deleted");
    }
}