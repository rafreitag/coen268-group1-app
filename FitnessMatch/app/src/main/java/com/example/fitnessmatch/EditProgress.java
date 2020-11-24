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

public class EditProgress extends AppCompatActivity {
    Button cancel_btn;
    Button delete_btn;
    Button update_btn;

    EditText current_goalET;

    RadioButton todo_rb;
    RadioButton in_progress_rb;
    RadioButton completed_rb;

    String prev_goal;
    String status;
    String user;
    Integer goal_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_progress);

        cancel_btn = findViewById(R.id.progress_cancel_btn);
        update_btn = findViewById(R.id.progress_update_btn);
        delete_btn = findViewById(R.id.del_btn);

        current_goalET = findViewById(R.id.current_goalET);
        todo_rb = findViewById(R.id.todo);
        in_progress_rb = findViewById(R.id.in_progress);
        completed_rb = findViewById(R.id.completed);

        Intent intent = getIntent();

        prev_goal = intent.getStringExtra("goal");
        status = intent.getStringExtra("status");
        user = intent.getStringExtra("user");
        goal_id = intent.getIntExtra("id", 0);

        current_goalET.setText(prev_goal);
        setRadioButton(status);



        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProgressListActivity();

            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGoalInDatabase();

            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteGoalInDatabase();

            }
        });


    }

    private void deleteGoalInDatabase() {

        UserGoalDBHelper dbHelper = new UserGoalDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = UserGoalContract.Goal.GOAL+ "=?" + " AND " + UserGoalContract.Goal.USER + "=?"
                + " AND " + UserGoalContract.Goal._ID + "=?";

        String[] whereArgs = {prev_goal, user, goal_id.toString()};

        int count = db.delete(UserGoalContract.Goal.TABLE_NAME,whereClause,whereArgs);
        db.close();

        if(count < 1){
            Toast.makeText(this, "No goal deleted",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Deleted goal",Toast.LENGTH_SHORT).show();
        }

        openProgressListActivity();
    }

    private void updateGoalInDatabase() {
        String goal = current_goalET.getText().toString();
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

        String whereClause = UserGoalContract.Goal.GOAL+ "=?" + " AND " + UserGoalContract.Goal.USER + "=?"
                + " AND " + UserGoalContract.Goal._ID + "=?";

        String[] whereArgs = {prev_goal, user, goal_id.toString()};

        int count =
                db.update(UserGoalContract.Goal.TABLE_NAME,contentValues,whereClause,whereArgs);
        db.close();

        if(count < 1){
            Toast.makeText(this,"No goal updated",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Updated "+count+" goals",Toast.LENGTH_SHORT).show();
        }


        openProgressListActivity();

    }

    private void setRadioButton(String status) {

        if (status.equals("completed")){
            completed_rb.setChecked(true);

        } else if(status.equals("todo")){
            todo_rb.setChecked(true);

        } else if(status.equals("in progress")){
            in_progress_rb.setChecked(true);
        }

    }

    private void openProgressListActivity() {
        Intent intent = new Intent(this, ProgressMainActivity.class);
        intent.putExtra("user", user);

        startActivity(intent);
    }
}