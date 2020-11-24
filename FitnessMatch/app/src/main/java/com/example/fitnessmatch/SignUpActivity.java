package com.example.fitnessmatch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class SignUpActivity extends AppCompatActivity {
    public final static String TAG = "SignUpActivity";

    private EditText et_name;
    private EditText et_email;
    private EditText et_password;
    private EditText confirm_password;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        confirm_password = findViewById(R.id.confirm_password);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
    }

    public void add_user(View view) {

        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String password_confirm = confirm_password.getText().toString();

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
        if(!password_confirm.equals(password)){
            Toast.makeText(this, "Password mismatch.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 6){
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(view.VISIBLE);

        // Create user in Firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();

                    // Get correct DB reference
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    DatabaseReference usersRef = myRef.child("users").child(user.getUid());

                    // Create Profile object to add to Firebase (name and email that the user inputted when signing up)
                    Profile profile = new Profile(name, email);
                    // Add Profile to Firebase
                    usersRef.child("profile").setValue(profile);

                    // Create Preferences object to add to Firebase
                    // This is just a default Preferences object that will be overwritten when a user goes through and fills out all of their preferences
                    Preferences preferences = new Preferences();
                    // Add preferences to Firebase
                    usersRef.child("preferences").setValue(preferences);

                    // Go to home page
                    Intent a = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(a);
                }
                else{
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(view.INVISIBLE);
                }
            }
        });

//        // 3: Add the record to the database
//        UserDbHelper dbHelper = new UserDbHelper(this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(UserContractInfo.Users.USER_NAME, name);
//        contentValues.put(UserContractInfo.Users.USER_EMAIL, email);
//        contentValues.put(UserContractInfo.Users.USER_PASSWORD, password);
//
//        long recordId = db.insert(UserContractInfo.Users.TABLE_NAME, null, contentValues);
//        db.close();
//
//        if(recordId == -1) {
//            Toast.makeText(this, "Insertion failed", Toast.LENGTH_SHORT).show();
//        } else {
//            Log.i(TAG, "Successfully added user " + name + " in the db");
//            Intent intent = new Intent(this, HomeActivity.class);
//            intent.putExtra("Name", name);
//            startActivity(intent);
//        }
    }

    public void back_to_login(View view){
        Intent a = new Intent(this, LoginActivity.class);
        startActivity(a);
    }
}