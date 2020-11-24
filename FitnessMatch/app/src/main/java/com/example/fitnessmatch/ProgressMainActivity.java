package com.example.fitnessmatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProgressMainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ListView listView;
    TextView noGoalsTV;

    List<String> mGoals = new ArrayList<String>();
    List<String> mStatuses = new ArrayList<String>();
    List<Integer> mImages = new ArrayList<Integer>();
    List<Integer> mIDs = new ArrayList<Integer>();
    String mUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_activity_main);

        noGoalsTV = findViewById(R.id.no_goals_tv);
        listView = findViewById(R.id.progressView);

        mGoals.clear();
        mStatuses.clear();
        mImages.clear();

        Intent intent = getIntent();
        mUser = intent.getStringExtra("user");

        getAllStudentInfo();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProgressListAdapter progressListAdapter = new ProgressListAdapter(this, mGoals.toArray(new String[mGoals.size()])
                , mStatuses.toArray(new String[mStatuses.size()]), mImages.toArray(new Integer[mImages.size()]), mUser );
        listView.setAdapter(progressListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                openEditProgressActivity(position);
            }
        });


        fab = findViewById(R.id.add_btn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddProgressActivity();

            }
        });


    }

    private void getAllStudentInfo() {
        // Completed 4: Retrieve the student info saved from the database
        UserGoalDBHelper dbHelper = new UserGoalDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "=?";
        String[] selectionArg = new String[]{mUser};

        Cursor cursor = db.query(UserGoalContract.Goal.TABLE_NAME,null,
                UserGoalContract.Goal.USER + "=?", new String[]{mUser}, null, null,
                UserGoalContract.Goal.STATUS);

        String result = "";
        while(cursor.moveToNext()){
            String goal = cursor.getString(cursor.getColumnIndex(UserGoalContract.Goal.GOAL));
            String status = cursor.getString(cursor.getColumnIndex(UserGoalContract.Goal.STATUS));
            int id = cursor.getInt(cursor.getColumnIndex(UserGoalContract.Goal._ID));

            mIDs.add(id);
            mGoals.add(goal);
            mStatuses.add(status);
        }
        db.close();

        setImageResources();

        if(mGoals.isEmpty() || mStatuses.isEmpty()){
            result = "No Goals Found";
        }
        noGoalsTV.setText(result);
    }

    private void setImageResources(){
        for (int i = 0; i < mStatuses.size(); i++){
            if(mStatuses.get(i).equals("completed")){
                mImages.add(R.drawable.complete);

            }else if(mStatuses.get(i).equals("in progress")){
                mImages.add(R.drawable.in_progress);

            }else if(mStatuses.get(i).equals("todo")){
                mImages.add(R.drawable.todo);
            }
        }

    }

    private void openAddProgressActivity() {
        Intent intent = new Intent(this, AddProgress.class);
        intent.putExtra("user", mUser);

        startActivity(intent);
    }

    private void openEditProgressActivity(int position) {
        Intent intent = new Intent(this, EditProgress.class);
        intent.putExtra("goal", mGoals.get(position));
        intent.putExtra("status", mStatuses.get(position));
        intent.putExtra("user", mUser);
        intent.putExtra("id", mIDs.get(position));
        startActivity(intent);
    }

    class ProgressListAdapter extends ArrayAdapter<String>{

        Context context;
        String rGoals[];
        String rStatuses[];
        Integer rImages[];
        String rUser;


        ProgressListAdapter(Context c, String goals[], String statuses[], Integer images[], String user){
            super(c, R.layout.progress_row, R.id.goal_descriptionTV, goals);
            this.context = c;
            this.rGoals = goals;
            this.rStatuses = statuses;
            this.rImages = images;
            this.rUser = user;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.progress_row, parent, false);
            TextView goalTV = row.findViewById(R.id.goal_descriptionTV);
            TextView statusTV = row.findViewById(R.id.statusTV);
            ImageView goalIV = row.findViewById(R.id.progressImageView);

            goalTV.setText(rGoals[position]);
            statusTV.setText(rStatuses[position]);
            goalIV.setImageResource((rImages[position]));



            return row;
        }

    }



}