package com.example.fitnessmatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WorkoutHistoryActivity extends AppCompatActivity {
    List<String> mGoals = new ArrayList<String>();
    List<String> mStatuses = new ArrayList<String>();
    List<Integer> mImages = new ArrayList<Integer>();
    List<Integer> mIDs = new ArrayList<Integer>();
    HashSet<Long> hSetDates;

    List<String> mGoalsAll = new ArrayList<String>();
    List<String> mStatusesAll = new ArrayList<String>();
    List<Integer> mImagesAll = new ArrayList<Integer>();
    List<Integer> mIDsAll = new ArrayList<Integer>();
    List<Long> mDatesAll = new ArrayList<Long>();

    ImageButton leftMap;
    ImageButton rightMap;

    String mUser = "";
    CompactCalendarView compactCalendar;
    TextView month_tv;
    TextView noGoalsTV;
    ListView listView;
    private SimpleDateFormat dataFormatMonthYear = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);
        mIDsAll.clear();
        mGoalsAll.clear();
        mStatusesAll.clear();
        mDatesAll.clear();

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        month_tv = (TextView) findViewById(R.id.monthTV);
        noGoalsTV = (TextView) findViewById(R.id.none_completedTV);
        listView = (ListView) findViewById(R.id.completedList);
        leftMap = (ImageButton) findViewById(R.id.leftCalenderButton);
        rightMap = (ImageButton) findViewById(R.id.rightCalenderButton);

        Intent intent = getIntent();
        mUser = intent.getStringExtra("user");

        compactCalendar.setUseThreeLetterAbbreviation(true);

        Date current_date = new Date();
        month_tv.setText(dataFormatMonthYear.format(current_date));
        compactCalendar.displayOtherMonthDays(true);

        getAllStudentInfo();

        addEvents();

        Date current = new Date();
        current.setSeconds(0);
        current.setMinutes(0);
        current.setHours(0);


        Long milli = current.getTime();

        String largeNumberInString = String.valueOf(milli);
        Long resultingNumber = Long.parseLong(largeNumberInString.substring(0, largeNumberInString.length()-3));
        milli = resultingNumber * 1000;
        getGoalInfoWithDate(milli);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WorkoutHistoryActivity.ProgressListAdapter progressListAdapter = new WorkoutHistoryActivity.ProgressListAdapter(this, mGoals.toArray(new String[mGoals.size()])
                , mStatuses.toArray(new String[mStatuses.size()]), mImages.toArray(new Integer[mImages.size()]), mUser );
        listView.setAdapter(progressListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                openEditProgressActivity(position);
            }
        });


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                Log.d("WUT", "date clicked: " + dateClicked);
                Long milli = dateClicked.getTime();
                Log.d("WUT", "this is: " + milli);
                getGoalInfoWithDate(milli);

                WorkoutHistoryActivity.ProgressListAdapter progressListAdapter = new WorkoutHistoryActivity.ProgressListAdapter(context, mGoals.toArray(new String[mGoals.size()])
                        , mStatuses.toArray(new String[mStatuses.size()]), mImages.toArray(new Integer[mImages.size()]), mUser );
                listView.setAdapter(progressListAdapter);

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                month_tv.setText(dataFormatMonthYear.format(firstDayOfNewMonth));

                Context context = getApplicationContext();
                Log.d("WUT", "date clicked: " + firstDayOfNewMonth);
                Long milli = firstDayOfNewMonth.getTime();
                Log.d("WUT", "this is: " + milli);
                getGoalInfoWithDate(milli);


                WorkoutHistoryActivity.ProgressListAdapter progressListAdapter = new WorkoutHistoryActivity.ProgressListAdapter(context, mGoals.toArray(new String[mGoals.size()])
                        , mStatuses.toArray(new String[mStatuses.size()]), mImages.toArray(new Integer[mImages.size()]), mUser );
                listView.setAdapter(progressListAdapter);

            }




        });



    }
    public void leftMap(View view){
        compactCalendar.scrollLeft();
    }
    public void rightMap(View view){
        compactCalendar.scrollRight();
    }




    private void getGoalInfoWithDate(Long milli) {
        mIDs.clear();
        mGoals.clear();
        mStatuses.clear();
        mImages.clear();

        String result = "";
        for(int i = 0; i < mDatesAll.size(); i++){
            if(milli.equals(mDatesAll.get(i))){
                mIDs.add(mIDsAll.get(i));
                mGoals.add(mGoalsAll.get(i));
                mStatuses.add(mStatusesAll.get(i));
            }

        }

        setImageResources();

        if(mGoals.isEmpty() || mStatuses.isEmpty()){
            result = "No Goals Completed";
        }
        noGoalsTV.setText(result);

    }

    private void addEvents() {

        for(Long date : hSetDates){
            if (date != -1L) {
                Event ev = new Event(Color.rgb(127,207, 118), date);
                compactCalendar.addEvent(ev);

            }

        }
    }


    private void getAllStudentInfo() {
        // Completed 4: Retrieve the student info saved from the database
        UserGoalDBHelper dbHelper = new UserGoalDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = UserGoalContract.Goal.USER + "=? AND " + UserGoalContract.Goal.STATUS + " =?";
        String[] selectionArg = new String[]{mUser, "completed"};

        String orderBy = "CASE " + UserGoalContract.Goal.STATUS +
                " WHEN 'completed' THEN 0 END";

        Cursor cursor = db.query(UserGoalContract.Goal.TABLE_NAME,null,
                selection, selectionArg, null, null,
                orderBy);

        String result = "";
        while(cursor.moveToNext()){
            Long date = cursor.getLong(cursor.getColumnIndex(UserGoalContract.Goal.DATE));
            String goal = cursor.getString(cursor.getColumnIndex(UserGoalContract.Goal.GOAL));
            String status = cursor.getString(cursor.getColumnIndex(UserGoalContract.Goal.STATUS));
            int id = cursor.getInt(cursor.getColumnIndex(UserGoalContract.Goal._ID));

            mIDsAll.add(id);
            mGoalsAll.add(goal);
            mStatusesAll.add(status);
            mDatesAll.add(date);
        }
        Log.d("HELL", "dates: " + mDatesAll);
        hSetDates = new HashSet(mDatesAll);
        Log.d("HELL", "hash dates: " + hSetDates);

        db.close();

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

    class ProgressListAdapter extends ArrayAdapter<String> {

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

    private void openEditProgressActivity(int position) {
        Intent intent = new Intent(this, EditProgress.class);
        intent.putExtra("goal", mGoals.get(position));
        intent.putExtra("status", mStatuses.get(position));
        intent.putExtra("user", mUser);
        intent.putExtra("id", mIDs.get(position));
        intent.putExtra("parent", "history");
        startActivity(intent);
    }


}
