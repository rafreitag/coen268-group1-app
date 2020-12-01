package com.example.fitnessmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class NewFindActivity extends AppCompatActivity {

    //Preferences currentUserPreferences; // = new Preferences(true, false, true, false, true, false, true, false, true, false, true, false, true, 1, 2, 3, 96.212, -121.323, "other");
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_find);
        mAuth = FirebaseAuth.getInstance();

        if (true) {
            createMatchDatabase();
        }
        else{
            //popup saying that you need to fill out your preferences first
            //takes you to filling out preferences page
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, FindListFragment.class, null)
                    .commit();
        }
    }



    public void createMatchDatabase(){

        String TAG = "createMatchDatabase";
        UserMatchDataDBHelper userMatchDataDBHelper = new UserMatchDataDBHelper(this);

        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference("users");

        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userMatchDataDBHelper.clearData();


                Preferences currentUserPreferences = snapshot.child(mAuth.getCurrentUser().getUid()).child("preferences").getValue(Preferences.class);

                for (DataSnapshot iterateSnapshot : snapshot.getChildren()) {


                    String user_id = iterateSnapshot.getKey();

                    Log.i(TAG, user_id +  " compared with " + mAuth.getCurrentUser().getUid());

                    if(!user_id.equals(mAuth.getCurrentUser().getUid())) {


                        Preferences preference = iterateSnapshot.child("preferences").getValue(Preferences.class);
                        Profile profile = iterateSnapshot.child("profile").getValue(Profile.class);


                        String user_name = profile.getName();

                        int match_score = currentUserPreferences.calculateMatchScore(preference);
                        double distance = currentUserPreferences.calculateDistanceFrom(preference);

                        Log.i(TAG, "COMPARED WITH USER: " + iterateSnapshot.child("profile").getValue(Profile.class).getName() +
                                "\nMatch Score = " + match_score +
                                "\nDistance = " + String.format("%.2f", distance));

                        userMatchDataDBHelper.addData(user_id, user_name, String.format("%.2f", distance), String.valueOf(match_score));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewFindActivity.this, "Read failed. Please retry login", Toast.LENGTH_LONG).show();
                // Logout User
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(NewFindActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}