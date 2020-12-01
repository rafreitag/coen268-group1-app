package com.example.fitnessmatch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserGoalDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "progress.db";
    private static final int DATABASE_VERSION = 8;
    private static final String CREATE_TABLE = "CREATE TABLE " +
            UserGoalContract.Goal.TABLE_NAME + "(" +
            UserGoalContract.Goal._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UserGoalContract.Goal.GOAL + " TEXT NOT NULL, " +
            UserGoalContract.Goal.STATUS + " TEXT NOT NULL, " +
            UserGoalContract.Goal.DATE + " LONG, " +
            UserGoalContract.Goal.USER + " TEXT NOT NULL" +
            ")";


    private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+
            UserGoalContract.Goal.TABLE_NAME;

    public UserGoalDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create tables
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop table
        sqLiteDatabase.execSQL(DROP_TABLE);
        //create
        onCreate(sqLiteDatabase);
    }

}
