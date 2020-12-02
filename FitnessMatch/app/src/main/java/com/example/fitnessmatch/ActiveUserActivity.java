package com.example.fitnessmatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ActiveUserActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_find);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, ActiveListFragment.class, null)
                    .commit();
        }
    }
}