package com.example.fitnessmatch;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sign_up(View view)
    {
        Intent a = new Intent(this, SignUpActivity.class);
        startActivity(a);
    }

    public void login(View view)
    {
        Intent a = new Intent(this, LoginActivity.class);
        startActivity(a);
    }
}