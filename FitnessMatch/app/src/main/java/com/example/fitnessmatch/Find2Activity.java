package com.example.fitnessmatch;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class Find2Activity extends AppCompatActivity {

    private RadioButton frequency1, frequency2, frequency3, frequency4;

    private Preferences preferences;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find2);
        mAuth = FirebaseAuth.getInstance();

        // Receive intent passed in from FindActivity
        Intent a = getIntent();
        // Receive preferences object passed in from LocationActivity (includes latitude and longitude)
        preferences = (Preferences) a.getSerializableExtra("preferences");

        // How often do you want to work out per week?
        frequency1 = findViewById(R.id.rb_frequency_1);
        frequency2 = findViewById(R.id.rb_frequency_2);
        frequency3 = findViewById(R.id.rb_frequency_3);
        frequency4 = findViewById(R.id.rb_frequency_4);
    }

    // More of a test than anything else to confirm that I know a user is actually logged in
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser == null){
//            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
//        }
    }

    public void nextPage(View view) {
        // Create intent to go to second page of find
        Intent a = new Intent(this, Find3Activity.class);
        // Update preferences object with all of the current preferences filled out during page 1
        update_preferences();
        // Sending this preferences object to the second find page so that none of the choices are lost
        a.putExtra("preferences", (Serializable) preferences);
        // Going to page 2
        startActivity(a);
    }

    // Set all preferences (for page 1)
    public void update_preferences() {
        // Set frequency
        if(frequency1.isChecked())
            preferences.setFrequency(1);
        else if(frequency2.isChecked())
            preferences.setFrequency(2);
        else if(frequency3.isChecked())
            preferences.setFrequency(3);
        else if(frequency4.isChecked())
            preferences.setFrequency(4);
    }
}
