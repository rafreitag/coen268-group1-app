package com.example.fitnessmatch;

import android.provider.BaseColumns;

public final class UserContractInfo {
    private UserContractInfo(){}

    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users_info";
        public static final String USER_NAME = "name";
        public static final String USER_EMAIL = "email";
        public static final String USER_PASSWORD = "password";
    }
}
