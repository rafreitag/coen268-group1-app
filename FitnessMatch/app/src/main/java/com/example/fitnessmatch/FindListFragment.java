package com.example.fitnessmatch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FindListFragment extends Fragment {

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_list, container, false);
        Log.i("LIST_FRAG", "fragment created");
        listView = view.findViewById(R.id.listViewUser);
        createMatchDatabase();

        return view;
    }

    public void populateListArray(UserMatchDataDBHelper dbHelper){


        ArrayList<MatchedUserItem> matchedUserList = new ArrayList<>();


        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //CAN DO FILTERING HERE EVENTUALLY INSTEAD OF QUERYING ALL
        String order = "match_score";

        Cursor cursor = db.rawQuery("SELECT * FROM " + UserMatchDataContract.MatchData.TABLE_NAME +
                " ORDER BY CAST(" + order + " AS INTEGER) DESC", null);

        cursor.moveToFirst();


        Log.i("LIST_FRAG", "SELECT * FROM " + UserMatchDataContract.MatchData.TABLE_NAME +
                " ORDER BY CAST(" + order + " AS INTEGER) DESC");

        //populate the list with whatever is in the local database
        while(!cursor.isAfterLast()){
            Log.i("LIST_FRAG", "HAS SOMETHING");
            int index = cursor.getColumnIndexOrThrow(UserMatchDataContract.MatchData.USER_ID);
            String user_id = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(UserMatchDataContract.MatchData.USER_NAME);
            String user_name = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(UserMatchDataContract.MatchData.DISTANCE);
            String distance = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow(UserMatchDataContract.MatchData.MATCH_SCORE);
            String match_score = cursor.getString(index);

            MatchedUserItem item = new MatchedUserItem(user_id, user_name, distance, match_score);
            matchedUserList.add(item);

            cursor.moveToNext();
        }

        MatchedUserItemAdapter matchedUserItemAdapter = new MatchedUserItemAdapter(getActivity(), R.layout.matched_user_item, matchedUserList);
        listView.setAdapter(matchedUserItemAdapter);
    }

    public void createMatchDatabase(){

        String TAG = "createMatchDatabase";

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        UserMatchDataDBHelper userMatchDataDBHelper = new UserMatchDataDBHelper(getActivity());

        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference("users");

        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userMatchDataDBHelper.clearData();

                Preferences currentUserPreferences = snapshot.child(mAuth.getCurrentUser().getUid()).child("preferences").getValue(Preferences.class);

                for (DataSnapshot iterateSnapshot : snapshot.getChildren()) {


                    String user_id = iterateSnapshot.getKey();

                    //Log.i(TAG, user_id +  " compared with " + mAuth.getCurrentUser().getUid());

                    if(!user_id.equals(mAuth.getCurrentUser().getUid())) {


                        Preferences preference = iterateSnapshot.child("preferences").getValue(Preferences.class);
                        Profile profile = iterateSnapshot.child("profile").getValue(Profile.class);


                        String user_name = profile.getName();

                        int match_score = currentUserPreferences.calculateMatchScore(preference);
                        double distance = currentUserPreferences.calculateDistanceFrom(preference);

//                        Log.i(TAG, "COMPARED WITH USER: " + iterateSnapshot.child("profile").getValue(Profile.class).getName() +
//                                "\nMatch Score = " + match_score +
//                                "\nDistance = " + String.format("%.2f", distance));

                        userMatchDataDBHelper.addData(user_id, user_name, String.format("%.2f", distance), String.valueOf(match_score));
                    }
                }
                populateListArray(userMatchDataDBHelper);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Read failed. Please retry login", Toast.LENGTH_LONG).show();
                // Logout User
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}