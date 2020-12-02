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
    }

    public void back_to_sign_up(View view){
        Intent a = new Intent(this, SignUpActivity.class);
        startActivity(a);
    }
}
