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
        myProfile.addValueEventListener(new ValueEventListener() {
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

        // Retrieve current 'preferences' of user
        DatabaseReference myPreferences = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("preferences");
        myPreferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Preferences preferences = snapshot.getValue(Preferences.class);
                // Just a test to make sure that I am receiving the information correctly
//                Toast.makeText(HomeActivity.this, preferences.getOther_activity(), Toast.LENGTH_SHORT).show();
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

        // Should retrieve all users if it works correctly
        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference("users");
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Map<String,Object> map = snapshot.getValue(Map.class);
//                for (Object key : map.keySet()) {
//                    System.out.println(key);
                for (DataSnapshot iterateSnapshot : snapshot.getChildren()) {
//                    Log.i("TEST", iterateSnapshot.child("preferences").getValue(Preferences.class).getOther_activity());
                    Preferences preferences = iterateSnapshot.child("preferences").getValue(Preferences.class);
                    Log.i("TEST", preferences.printData());
                }
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
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Start find process
    public void find(View view){
//        Intent intent = new Intent(this, LocationActivity.class);
//        startActivity(intent);
        Intent intent = new Intent(this, NewFindActivity.class);
        startActivity(intent);
    }

    // Start find progress
    public void progress(View view){
        Intent intent = new Intent(this, ProgressMainActivity.class);
        intent.putExtra("user", email);
        startActivity(intent);
    }



}
