package com.miryanova.memories.model;

import android.provider.BaseColumns;

public final class AuthorizationContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private AuthorizationContract() {
    }

    /* Inner class that defines the table contents */
    public static class AuthorizationEntry implements BaseColumns {
        public static final String TABLE_NAME = "authorization";

        public static final String _ID = BaseColumns._ID;
        public static final String LOGIN = "Login";
        public static final String PASSWORD = "Password";
        public static final String EMAIL = "Email";
    }
}
