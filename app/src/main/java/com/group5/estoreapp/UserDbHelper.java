package com.group5.estoreapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group5.estoreapp.model.User;

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "estore.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "User";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PHONE = "phoneNumber";
    private static final String COL_ADDRESS = "address";
    private static final String COL_ROLE = "role";

    public UserDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_ROLE + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        clearUser();
        ContentValues values = new ContentValues();
        values.put(COL_ID, user.getUserID());
        values.put(COL_USERNAME, user.getUsername());
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_PHONE, user.getPhoneNumber());
        values.put(COL_ADDRESS, user.getAddress());
        values.put(COL_ROLE, user.getRole());
        db.insert(TABLE_NAME, null, values);
    }

    public void clearUser() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public User getUser() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setUserID(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }
}
