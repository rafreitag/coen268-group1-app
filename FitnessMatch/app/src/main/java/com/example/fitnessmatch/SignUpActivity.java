package com.example.fitnessmatch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    public final static String TAG = "SignUpActivity";

    private EditText et_name;
    private EditText et_email;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
    }

    public void add_user(View view) {
        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 3: Add the record to the database
        UserDbHelper dbHelper = new UserDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserContractInfo.Users.USER_NAME, name);
        contentValues.put(UserContractInfo.Users.USER_EMAIL, email);
        contentValues.put(UserContractInfo.Users.USER_PASSWORD, password);

        long recordId = db.insert(UserContractInfo.Users.TABLE_NAME, null, contentValues);
        db.close();

        if(recordId == -1) {
            Toast.makeText(this, "Insertion failed", Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "Successfully added user " + name + " in the db");
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("Name", name);
            startActivity(intent);
        }
    }
}
