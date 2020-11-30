package com.example.fitnessmatch;

import android.provider.BaseColumns;

public final class UserMatchDataContract {

    //avoid accidental instantiation
    private UserMatchDataContract(){}

    public static class MatchData implements BaseColumns {
        public static final String TABLE_NAME = "match_table";
        public static final String USER_NAME = "user_name";
        public static final String USER_ID = "user_id";
        public static final String DISTANCE = "distance";
        public static final String MATCH_SCORE = "match_score";

    }

}
