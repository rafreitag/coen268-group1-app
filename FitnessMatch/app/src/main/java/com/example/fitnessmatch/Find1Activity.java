package com.example.fitnessmatch;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class Find1Activity extends AppCompatActivity {

    private CheckBox running, walking, yoga, biking, weight_lifting, swimming, other, hiking, pilates, circuit_training, basketball, volleyball, ultimate_frisbee, soccer;
    private EditText other_activity;

    private Preferences preferences;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find1);
        mAuth = FirebaseAuth.getInstance();

        // Receive intent passed in from FindActivity
        Intent a = getIntent();
        // Receive preferences object passed in from LocationActivity (includes latitude and longitude)
        preferences = (Preferences) a.getSerializableExtra("preferences");

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
    }

    // More of a test than anything else to confirm that I know a user is actually logged in
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void nextPage(View view) {
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
    }
}
