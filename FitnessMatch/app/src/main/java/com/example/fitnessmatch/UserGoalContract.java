package com.example.fitnessmatch;

import android.provider.BaseColumns;

public final class UserGoalContract {

    //avoid accidental instantiation
    private UserGoalContract(){}

    public static class Goal implements BaseColumns {
        public static final String TABLE_NAME = "user_progress_info";
        public static final String GOAL = "goal";
        public static final String STATUS = "status";

    }

}
