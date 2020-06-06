package com.miryanova.memories.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.miryanova.memories.model.User;
import com.miryanova.memories.model.AuthorizationContract.AuthorizationEntry;


public class AuthorizationController {
    private Context context;
    private AuthorizationDataBaseHelper dataBaseHelper;
    private String login;
    private String password;
    private String email;

    public AuthorizationController(Context context) {
        this.context = context;
        dataBaseHelper = new AuthorizationDataBaseHelper(context);
    }

    public void enter(String _login, String _password) throws CustomException {
        validate(_login, _password);
        User.getUser(context).setLogin(login);
        User.getUser(context).setPassword(password);
        User.getUser(context).setEmail(email);
    }

    private void validate(String _login, String _password) throws CustomException {
        if (_login.isEmpty() || _password.isEmpty())
            throw new CustomException("Fields mustn't be empty");
        else if (!doSelect(_login, _password))
            throw new CustomException("Wrong login or password");
    }

    public void register(String _login, String _password, String _email) throws CustomException {
        if (validateRegistration(_login, _password, _email))
            doInsert(_login, _password, _email);
    }

    private boolean validateRegistration(String _login, String _password, String _email) throws CustomException {
        if (_login.isEmpty() || _password.isEmpty() || _email.isEmpty())
            throw new CustomException("Field mustn't be empty");
        else if (_password.length() < 6)
            throw new CustomException("Password must be longer than 6 characters");
        else if (!isValidEmail(_email))
            throw new CustomException("Wrong e-mail address");
        else if (doSelect(_login, _password))
            throw new CustomException("This login is already used");
        else return true;
    }

    private boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target)) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Функция нахождения в базе данных е-мейла по логину и паролю
     * Используется также для проверки дублирования логинов
     *
     * @param _login
     * @param _password
     * @return
     */
    private boolean doSelect(String _login, String _password) {
        String selection;
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        String[] projection = {
                AuthorizationEntry.EMAIL,};
        String[] selectionArgs;
        selection = AuthorizationEntry.LOGIN + "=? AND " + AuthorizationEntry.PASSWORD + "=?";
        selectionArgs = new String[]{_login, _password};
        Cursor cursor = db.query(
                AuthorizationEntry.TABLE_NAME, // таблица
                projection,            // столбцы
                selection,             // столбцы для условия WHERE
                selectionArgs,         // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                AuthorizationEntry.EMAIL + " DESC");  // порядок сортировки
        try {
            while (cursor.moveToNext()) {
                String currentEmail = cursor.getString(cursor.getColumnIndex(AuthorizationEntry.EMAIL));
                if (currentEmail.isEmpty())
                    return false;
                else {
                    login = _login;
                    password = _password;
                    email = currentEmail;
                    return true;
                }
            }
        } finally {
            cursor.close();
        }
        return false;
    }

    /**
     * Функция добавления пользователя в базу данных
     *
     * @param _login
     * @param _password
     * @param _email
     */

    private void doInsert(String _login, String _password, String _email) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AuthorizationEntry.LOGIN, _login);
        values.put(AuthorizationEntry.PASSWORD, _password);
        values.put(AuthorizationEntry.EMAIL, _email);

        db.insert(AuthorizationEntry.TABLE_NAME, null, values);
    }

    /**
     * Функция удаления пользователя из базы данных
     *
     * @param _login
     * @param _password
     */

    public void delete(String _login, String _password) {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        db.delete(AuthorizationEntry.TABLE_NAME, "Login=? and Password=?", new String[]{_login, _password});
    }
}