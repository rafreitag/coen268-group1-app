package com.example.fitnessmatch;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class FindActivity extends AppCompatActivity {

    private CheckBox running, walking, yoga, biking, weight_lifting, swimming, other, hiking, pilates, circuit_training, basketball, volleyball, ultimate_frisbee, soccer;
    private EditText other_activity;
    private RadioButton frequency1, frequency2, frequency3, frequency4;

    private Preferences preferences;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.find);
        mAuth = FirebaseAuth.getInstance();

        preferences = new Preferences();

        // How do you like working out?
        running = findViewById(R.id.cb_running);
        walking = findViewById(R.id.cb_walking);
        yoga = findViewById(R.id.cb_yoga);
        biking = findViewById(R.id.cb_biking);
        weight_lifting = findViewById(R.id.cb_weight_lifting);
        swimming = findViewById(R.id.cb_swimming);
        other = findViewById(R.id.cb_other);
        other_activity = findViewById(R.id.other_activity);
        hiking = findViewById(R.id.cb_hiking);
        pilates = findViewById(R.id.cb_pilates);
        circuit_training = findViewById(R.id.cb_circuit_training);
        basketball = findViewById(R.id.cb_basketball);
        volleyball = findViewById(R.id.cb_volleyball);
        ultimate_frisbee = findViewById(R.id.cb_ultimate_frisbee);
        soccer = findViewById(R.id.cb_soccer);

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
        if(currentUser == null){
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
        }
    }

    public void page2(View view){
        // Create intent to go to second page of find
        Intent a = new Intent(this, Find2Activity.class);
        // Update preferences object with all of the current preferences filled out during page 1
        update_preferences();
        // Sending this preferences object to the second find page so that none of the choices are lost
        a.putExtra("preferences", (Serializable) preferences);
        // Going to page 2
        startActivity(a);
    }

    // Set all preferences (for page 1)
    public void update_preferences(){
        // Set Work out type preferences
        preferences.setRunning(running.isChecked());
        preferences.setWalking(walking.isChecked());
        preferences.setYoga(yoga.isChecked());
        preferences.setBiking(biking.isChecked());
        preferences.setWeight_lifting(weight_lifting.isChecked());
        preferences.setSwimming(swimming.isChecked());
        preferences.setHiking(hiking.isChecked());
        preferences.setPilates(pilates.isChecked());
        preferences.setCircuit_training(circuit_training.isChecked());
        preferences.setBasketball(basketball.isChecked());
        preferences.setVolleyball(volleyball.isChecked());
        preferences.setUltimate_frisbee(ultimate_frisbee.isChecked());
        preferences.setSoccer(soccer.isChecked());
        if(other.isChecked())
            preferences.setOther_activity(other_activity.getText().toString());

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
