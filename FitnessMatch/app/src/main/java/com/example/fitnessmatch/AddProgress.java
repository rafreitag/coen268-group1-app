package com.example.fitnessmatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddProgress extends AppCompatActivity {
    Button cancel_btn;
    Button add_btn;
    EditText goal_input;
    RadioButton todo_rb;
    RadioButton in_progress_rb;
    RadioButton completed_rb;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_progress);

        cancel_btn = findViewById(R.id.progress_cancel_btn);
        add_btn = findViewById(R.id.progress_update_btn);
        goal_input = findViewById(R.id.current_goalET);
        todo_rb = findViewById(R.id.todo);
        in_progress_rb = findViewById(R.id.in_progress);
        completed_rb = findViewById(R.id.completed);

        todo_rb.setChecked(true);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProgressListActivity();

            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProgressIntoDatabase();
            }
        });

    }

    private void saveProgressIntoDatabase() {
        String goal = goal_input.getText().toString();
        String status = "";

        if(goal.isEmpty()) {
            Toast.makeText(this, "Goal cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(completed_rb.isChecked()){
            status = "completed";
        }else if(in_progress_rb.isChecked()){
            status = "in progress";
        }else if(todo_rb.isChecked()){
            status = "todo";
        }

        UserGoalDBHelper dbHelper = new UserGoalDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserGoalContract.Goal.GOAL, goal);
        contentValues.put(UserGoalContract.Goal.STATUS, status);
        contentValues.put(UserGoalContract.Goal.USER, user);

        long recordId =
                db.insert(UserGoalContract.Goal.TABLE_NAME,null,contentValues);
        db.close();

        if(recordId == -1){
            Toast.makeText(this, "Insertion failed",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Successfully added goal",Toast.LENGTH_SHORT).show();
        }

        openProgressListActivity();
    }

    private void openProgressListActivity() {
        Intent intent = new Intent(this, ProgressMainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

}