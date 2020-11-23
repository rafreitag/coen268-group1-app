package com.example.fitnessmatch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    public final static String TAG = "LoginActivity";

    private EditText et_email;
    private EditText et_password;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_check_email);
        et_password = findViewById(R.id.et_check_password);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);
    }

    public void check_credentials(View view)
    {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and/or password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(view.VISIBLE);
        Log.i(TAG, "Email: " + email + ", Password: " + password);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, go to home page
                            Log.d(TAG, "signInWithEmail:success");
                            Intent a = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(a);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Email or password mismatch", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(view.INVISIBLE);
                        }
                    }
                });
//        UserDbHelper dbHelper = new UserDbHelper(this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        String[] columns = {UserContractInfo.Users.USER_NAME, UserContractInfo.Users.USER_EMAIL, UserContractInfo.Users.USER_PASSWORD};
//        String selection = UserContractInfo.Users.USER_EMAIL + "=?";
//        String[] selectionArgs = {email};
//        Cursor cursor = db.query(UserContractInfo.Users.TABLE_NAME, columns, selection, selectionArgs,
//                null, null, UserContractInfo.Users.USER_EMAIL);
//
//        int count = cursor.getCount();
//        Log.i(TAG, "Count: " + count);
//        String result = cursor.getString(cursor.getColumnIndex(UserContractInfo.Users.USER_PASSWORD));
//        String name = cursor.getString(cursor.getColumnIndex(UserContractInfo.Users.USER_NAME));
//        Log.i(TAG, "Password: " + result);
//        db.close();
//
//        if(result.isEmpty()) {
//            Log.i(TAG, "No user exists with that email.");
//            Toast.makeText(this, "No user exists with that email.", Toast.LENGTH_SHORT).show();
//            return;
//        } else if(result != password) {
//            Log.i(TAG, "Incorrect password.");
//            Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
//            // login
//            // start second activity
//            Log.i(TAG, "Successfully logged in as " + name);
//            Intent intent = new Intent(this, HomeActivity.class);
//            intent.putExtra("Name", name);
//            startActivity(intent);
//        }
    }

    public void back_to_sign_up(View view){
        Intent a = new Intent(this, SignUpActivity.class);
        startActivity(a);
    }
}
