package com.example.fitnessmatch;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static String EMAIL = "test@gmail.com";
    public static String PASSWORD = "Password1";

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void check_credentials(View view)
    {
        if(!email.getText().toString().equals(email) || !password.getText().toString().equals(PASSWORD)) {
            Toast.makeText(getApplicationContext(), "Incorrect Username or Password", Toast.LENGTH_LONG).show();
            return;
        }
        setContentView(R.layout.find);
    }

    public void page2(View view)
    {
        setContentView(R.layout.find2);
    }
}
