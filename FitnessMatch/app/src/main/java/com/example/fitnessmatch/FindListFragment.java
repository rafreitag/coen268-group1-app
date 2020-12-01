package com.example.fitnessmatch;

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

        ArrayList<MatchedUserItem> matchedUserList = new ArrayList<>();

        UserMatchDataDBHelper dbHelper = new UserMatchDataDBHelper(getActivity());
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

        return view;
    }
}