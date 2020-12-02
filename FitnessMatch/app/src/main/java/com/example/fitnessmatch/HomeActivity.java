package com.example.fitnessmatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private String name = "";
    private String email = "";
    private TextView welcome;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        welcome = findViewById(R.id.tv_welcome_name);
        FirebaseUser user = mAuth.getCurrentUser();

        // Retrieve current 'profile' information of user (name and email)
        DatabaseReference myProfile = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("profile");
        myProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.getValue(Profile.class);
                name = profile.getName();
                email = profile.getEmail();
                welcome.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Read failed. Please retry login", Toast.LENGTH_LONG).show();
                // Logout User
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        DatabaseReference myRequests = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("requests");
//        myRequests.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//
//                myRequests.child("32fWef94f3s").setValue("sent");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });





    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // view match list
    public void matchList(View view){
//        Intent intent = new Intent(this, LocationActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(this, NewFindActivity.class);
        startActivity(intent);
    }

    // progress activity
    public void progress(View view){
        Intent intent = new Intent(this, ProgressMainActivity.class);
        intent.putExtra("user", email);
        startActivity(intent);
    }

    // Start workout history
    public void history(View view){
        Intent intent = new Intent(this, WorkoutHistoryActivity.class);
        intent.putExtra("user", email);
        startActivity(intent);
    }

    // progress activity
    public void updatePreferences(View view){
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra("user", email);
        startActivity(intent);
    }

    // requests activity
    public void viewRequests(View view){
        Intent intent = new Intent(this, RequestsActivity.class);
        startActivity(intent);
    }

    // active users activity
    public void viewActiveUsers(View view){
        Intent intent = new Intent(this, ActiveUserActivity.class);
        startActivity(intent);
    }



}
