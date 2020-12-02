package com.example.fitnessmatch;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Find2Activity extends AppCompatActivity {

    private TextView tv_freq_label, tv_time_label, tv_level_label;
    private SeekBar seek_bar1, seek_bar2, seek_bar3;
//    private RadioButton frequency1, frequency2, frequency3, frequency4;
    private int frequency, pref_time, level;

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

        // set a change listener on the SeekBar
        seek_bar1 = findViewById(R.id.seek_bar1);
        seek_bar1.setOnSeekBarChangeListener(seekBar1ChangeListener);

        int progress1 = seek_bar1.getProgress();
        tv_freq_label = findViewById(R.id.tv_freq_label);
        tv_freq_label.setText(progress1 + " times per week");

        seek_bar2 = findViewById(R.id.seek_bar2);
        seek_bar2.setOnSeekBarChangeListener(seekBar2ChangeListener);

        tv_time_label = findViewById(R.id.tv_time_label);
        tv_time_label.setText("2 pm");

        seek_bar3 = findViewById(R.id.seek_bar3);
        seek_bar3.setOnSeekBarChangeListener(seekBar3ChangeListener);

        tv_level_label = findViewById(R.id.tv_level_label);
        tv_level_label.setText("Intermediate");

        // How often do you want to work out per week?
//        frequency1 = findViewById(R.id.rb_frequency_1);
//        frequency2 = findViewById(R.id.rb_frequency_2);
//        frequency3 = findViewById(R.id.rb_frequency_3);
//        frequency4 = findViewById(R.id.rb_frequency_4);
    }

    SeekBar.OnSeekBarChangeListener seekBar1ChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            frequency = progress;
            tv_freq_label.setText(progress + " times per week");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    SeekBar.OnSeekBarChangeListener seekBar2ChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            String code = "am";
            int time = progress;
            if(time == 12) {
                code = "pm";
            } else if(time > 12) {
                time = time - 12;
                code = "pm";
            }
            pref_time = progress;
            tv_time_label.setText(time + " " + code);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    SeekBar.OnSeekBarChangeListener seekBar3ChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            String str_level = "";
            if(progress == 1) {
                str_level = "Beginner";
                level = 1;
            } else if(progress == 2) {
                str_level = "Intermediate";
                level = 2;
            } else if(progress == 3) {
                str_level = "Advanced";
                level = 3;
            }
            tv_level_label.setText(str_level);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

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

    // Set all preferences (for page 1)
    public void update_preferences() {
        // Set frequency
        preferences.setFrequency(frequency);
        preferences.setPref_time(pref_time);
        preferences.setLevel(level);

//        if(frequency1.isChecked())
//            preferences.setFrequency(1);
//        else if(frequency2.isChecked())
//            preferences.setFrequency(2);
//        else if(frequency3.isChecked())
//            preferences.setFrequency(3);
//        else if(frequency4.isChecked())
//            preferences.setFrequency(4);
    }

    // Adds all preferences (from page 1 and page 2 of find) to Firebase and then redirects to home page
    public void updatePreferences(View view){

        // Update preferences object with all of the current preferences filled out during page 1 and page 2
        update_preferences();
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
