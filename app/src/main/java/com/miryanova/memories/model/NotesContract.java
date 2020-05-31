package com.miryanova.memories.model;

import android.provider.BaseColumns;

public final class NotesContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private NotesContract() {
    }

    /* Inner class that defines the table contents */
    public static class NotesEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";

        public static final String _ID = BaseColumns._ID;
        public static final String UUID = "UUID";
        public static final String LOGIN = "Login";
        public static final String DATE = "Date";
        public static final String TITLE = "Title";
        public static final String CONTENT = "Content";
        public static final String RESOURCES = "Resources";
    }
}
