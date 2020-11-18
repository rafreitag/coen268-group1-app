package com.example.fitnessmatch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE " +
            UserContractInfo.Users.TABLE_NAME + "(" +
            UserContractInfo.Users._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            UserContractInfo.Users.USER_NAME + " TEXT NOT NULL, " +
            UserContractInfo.Users.USER_EMAIL + " TEXT NOT NULL, " +
            UserContractInfo.Users.USER_PASSWORD + " TEXT" + ")";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + UserContractInfo.Users.TABLE_NAME;

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
