//  I FEEL LIKE THE SECOND PAGE FOR FIND SHOULD BE A FRAGMENT
//  AND PART OF THE FIRST ACTIVITY IN SOME WAY BUT I HONESTLY
//  DON'T KNOW HOW TO IMPLEMENT FRAGMENTS AT ALL SO FOR NOW IT IS
//  SIMPLY ANOTHER ACTIVITY. WE CAN MAYBE SEE IF WE CAN CHANGE THIS
//  BEFORE WE TURN IN THE FINAL PRODUCT BUT FOR NOW I THINK THIS
//  TECHNICALLY WORKS SO IM NOT GOING TO STRESS OVER IT.

package com.example.fitnessmatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Find2Activity extends AppCompatActivity {
    private RadioButton pref_time1, pref_time2, pref_time3, pref_time4, pref_time5;
    private RadioButton level1, level2, level3;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private Preferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.find2);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar3);

        // Receive intent passed in from FindActivity
        Intent a = getIntent();
        // Receive preferences object passed in from FindActivity (includes all user filled out preferences from page 1)
        preferences = (Preferences) a.getSerializableExtra("preferences");

        // What time of day do you like to work out?
        pref_time1 = findViewById(R.id.rb_pref_time_1);
        pref_time2 = findViewById(R.id.rb_pref_time_2);
        pref_time3 = findViewById(R.id.rb_pref_time_3);
        pref_time4 = findViewById(R.id.rb_pref_time_4);
        pref_time5 = findViewById(R.id.rb_pref_time_5);

        // What is your fitness level?
        level1 = findViewById(R.id.rb_level_1);
        level2 = findViewById(R.id.rb_level_2);
        level3 = findViewById(R.id.rb_level_3);
    }

    // Set all preferences (for page 2)
    public void update_preferences2(){
        // Set pref_time
        if(pref_time1.isChecked())
            preferences.setPref_time(1);
        else if(pref_time2.isChecked())
            preferences.setPref_time(2);
        else if(pref_time3.isChecked())
            preferences.setPref_time(3);
        else if(pref_time4.isChecked())
            preferences.setPref_time(4);
        else if(pref_time5.isChecked())
            preferences.setPref_time(5);

        // Set level
        if(level1.isChecked())
            preferences.setLevel(1);
        else if(level2.isChecked())
            preferences.setLevel(2);
        else if(level3.isChecked())
            preferences.setLevel(3);
    }

    // NEED TO RETRIEVE ALL USER DATA AT THIS POINT SO WE CAN DO THE MATCH STUFF BUT I HAVE NOT QUITE FIGURED OUT HOW TO DO THAT
    // THE CODE TO RETRIEVE ONE SPECIFIC USER PROFILE IS IN HOMEACTIVITY.JAVA
    // Adds all preferences (from page 1 and page 2 of find) to Firebase and then redirects to home page
    public void find_matches(View view){
        progressBar.setVisibility(View.VISIBLE);
        // Update preferences object with all of the current preferences filled out during page 1 and page 2
        update_preferences2();
        // Get correct DB reference
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        DatabaseReference usersRef = myRef.child("users").child(user.getUid());
        // Add preferences to Firebase
        usersRef.child("preferences").setValue(preferences);
        progressBar.setVisibility(View.INVISIBLE);
        // Go to home page
        Intent a = new Intent(this, HomeActivity.class);
        startActivity(a);
    }
}
