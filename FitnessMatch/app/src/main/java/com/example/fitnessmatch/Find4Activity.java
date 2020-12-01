package com.example.fitnessmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Find4Activity extends AppCompatActivity {
    private RadioButton level1, level2, level3;
//    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private Preferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find4);
        mAuth = FirebaseAuth.getInstance();
//        progressBar = findViewById(R.id.progressBar3);

        // Receive intent passed in from FindActivity
        Intent a = getIntent();
        // Receive preferences object passed in from FindActivity (includes all user filled out preferences from page 1)
        preferences = (Preferences) a.getSerializableExtra("preferences");

        // What is your fitness level?
        level1 = findViewById(R.id.rb_level_1);
        level2 = findViewById(R.id.rb_level_2);
        level3 = findViewById(R.id.rb_level_3);
    }

    // Set all preferences (for page 2)
    public void update_preferences2(){
        // Set level
        if(level1.isChecked())
            preferences.setLevel(1);
        else if(level2.isChecked())
            preferences.setLevel(2);
        else if(level3.isChecked())
            preferences.setLevel(3);
    }
    
    // Adds all preferences (from page 1 and page 2 of find) to Firebase and then redirects to home page
    public void updatePreferences(View view){

        // Update preferences object with all of the current preferences filled out during page 1 and page 2
        update_preferences2();
        // Get correct DB reference

        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference usersRef = myRef.child("users").child(user.getUid());
        // Add preferences to Firebase
        usersRef.child("preferences").setValue(preferences);

        // Go to home page

        Toast.makeText(this, "Preferences Successfully Updated", Toast.LENGTH_SHORT).show();

        Intent a = new Intent(this, HomeActivity.class);
        startActivity(a);
    }
}

