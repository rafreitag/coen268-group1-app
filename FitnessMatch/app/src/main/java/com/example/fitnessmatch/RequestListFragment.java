package com.example.fitnessmatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestListFragment extends Fragment {
    ListView listView;
    List<String> requestIDs = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_list, container, false);
        Log.i("LIST_FRAG", "fragment created");
        listView = view.findViewById(R.id.listViewUser);
        getRequests();
        populateList();

        //sendRequestTo("0Gi8Xeb4nhPYg28JkKg22fRZbQJ2");

        return view;
    }

    public void getRequests(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid()).child("requests");

        myDatabase.addValueEventListener(new ValueEventListener(){
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot iterateSnapshot : snapshot.getChildren()) {
                    String user_id = iterateSnapshot.getKey();
                    String value = iterateSnapshot.getValue(String.class);
                    if(value.equals("RECV")){
                        Log.i("Recieving Requests", user_id +  " " + value);
                        requestIDs.add(user_id);
                    }
                }
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

    public void populateList(){
        ArrayList<MatchedUserItem> matchedUserList = new ArrayList<>();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference("users");

        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // One's own preferences
                Preferences currentUserPreferences = snapshot.child(mAuth.getCurrentUser().getUid()).child("preferences").getValue(Preferences.class);
                for (DataSnapshot iterateSnapshot : snapshot.getChildren()) {
                    String user_id = iterateSnapshot.getKey();
                    if(requestIDs.contains(user_id)) {
                        Preferences preference = iterateSnapshot.child("preferences").getValue(Preferences.class);
                        Profile profile = iterateSnapshot.child("profile").getValue(Profile.class);
                        String user_name = profile.getName();

                        int match_score = currentUserPreferences.calculateMatchScore(preference);
                        double distance = currentUserPreferences.calculateDistanceFrom(preference);

                        MatchedUserItem item = new MatchedUserItem(user_id, user_name, String.format("%.2f", distance), String.valueOf(match_score));
                        matchedUserList.add(item);
                    }
                    //Log.i(TAG, user_id +  " compared with " + mAuth.getCurrentUser().getUid());
//                    if(!user_id.equals(mAuth.getCurrentUser().getUid())) {
//                        Preferences preference = iterateSnapshot.child("preferences").getValue(Preferences.class);
//                        Profile profile = iterateSnapshot.child("profile").getValue(Profile.class);
//                        String user_name = profile.getName();
//
//                        int match_score = currentUserPreferences.calculateMatchScore(preference);
//                        double distance = currentUserPreferences.calculateDistanceFrom(preference);
//
////                        Log.i(TAG, "COMPARED WITH USER: " + iterateSnapshot.child("profile").getValue(Profile.class).getName() +
////                                "\nMatch Score = " + match_score +
////                                "\nDistance = " + String.format("%.2f", distance));

                    }
                RequestedUserItemAdapter requestedUserItemAdapter = new RequestedUserItemAdapter(getActivity(), R.layout.request_user_item, matchedUserList);
                listView.setAdapter(requestedUserItemAdapter);
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
