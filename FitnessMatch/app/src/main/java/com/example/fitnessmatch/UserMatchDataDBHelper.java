package com.example.fitnessmatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.firestore.auth.User;

public class UserMatchDataDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "UserMatchDataDBHelper";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "match_data.db";

    private static final String CREATE_TABLE = "CREATE TABLE " +
            UserMatchDataContract.MatchData.TABLE_NAME + "(" +
            UserMatchDataContract.MatchData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UserMatchDataContract.MatchData.USER_ID + " TEXT NOT NULL, " +
            UserMatchDataContract.MatchData.USER_NAME + " TEXT NOT NULL, " +
            UserMatchDataContract.MatchData.DISTANCE + " TEXT NOT NULL, " +
            UserMatchDataContract.MatchData.MATCH_SCORE + " TEXT NOT NULL" +
            ")";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " +
            UserMatchDataContract.MatchData.TABLE_NAME;


    public UserMatchDataDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(TAG, CREATE_TABLE);

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop table
        sqLiteDatabase.execSQL(DROP_TABLE);
        //create
        onCreate(sqLiteDatabase);
    }

    public boolean addData(String user_id, String user_name, String distance, String match_score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(UserMatchDataContract.MatchData.USER_ID, user_id);
        contentValues.put(UserMatchDataContract.MatchData.USER_NAME, user_name);
        contentValues.put(UserMatchDataContract.MatchData.DISTANCE, distance);
        contentValues.put(UserMatchDataContract.MatchData.MATCH_SCORE, match_score);

        long result = db.insert(UserMatchDataContract.MatchData.TABLE_NAME, null, contentValues);

        db.close();

        if (result == -1 ) {
            Log.d(TAG, "Insertion Failed");
            return false;
        }
        else {
            Log.d(TAG, "Adding UserID: " + user_id +
                    "\n Name: " + user_name +
                    "\n Distance: " + distance +
                    "\n Match Score: " + match_score +
                    "\n in " + UserMatchDataContract.MatchData.TABLE_NAME);
            return true;
        }


    }

    public void clearData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE);
        db.close();


    }



}
