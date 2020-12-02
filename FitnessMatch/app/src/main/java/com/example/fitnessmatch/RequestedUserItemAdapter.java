package com.example.fitnessmatch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestedUserItemAdapter extends ArrayAdapter<MatchedUserItem> {
    private Context mContext;
    private int mResource;

    public RequestedUserItemAdapter(Context context, int resource,  @NonNull ArrayList<MatchedUserItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String user_id = getItem(position).getUser_id();
        String name = getItem(position).getName();
        String distance = getItem(position).getDistance();
        String match_score = getItem(position).getMatch_score();

        //MatchedUserItem matchedUserItem = new MatchedUserItem(user_id, name, distance, match_score);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tv_user_name = (TextView) convertView.findViewById(R.id.name);
        TextView tv_distance = (TextView) convertView.findViewById(R.id.distance);
        TextView tv_match_score = (TextView) convertView.findViewById(R.id.match_score);
        Button btn_accept = (Button) convertView.findViewById(R.id.btn_accept);
        Button btn_decline = (Button) convertView.findViewById(R.id.btn_decline);

        tv_user_name.setText(name);
        tv_distance.setText(distance + "mi");
        tv_match_score.setText(match_score + "% match");

        int int_match_score = Integer.parseInt(match_score);

        if (int_match_score >=  80){
            tv_match_score.setTextColor(mContext.getColor(R.color.green));
        }
        else if (int_match_score <  80 && int_match_score >=  40){
            tv_match_score.setTextColor(mContext.getColor(R.color.orange));
        }

        else if (int_match_score <  40 && int_match_score >=  0){
            tv_match_score.setTextColor(mContext.getColor(R.color.red));
        }

        //button tagging
        //https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept_request(user_id);
                btn_accept.setEnabled(false);
                btn_decline.setEnabled(false);
            }
        });
        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline_request(user_id);
                btn_accept.setEnabled(false);
                btn_decline.setEnabled(false);
            }
        });
        return convertView;
    }

    public void accept_request(String user_id){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Update current user's requests
        DatabaseReference myReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).child("requests");
        myReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myReference.child(user_id).setValue("ACPT");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Update the other user's requests
        DatabaseReference otherReference = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("requests");
        otherReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                otherReference.child(mAuth.getCurrentUser().getUid()).setValue("ACPT");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(getContext(), "Partner Accepted", Toast.LENGTH_SHORT).show();
    }

    public void decline_request(String user_id){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Update current user's requests
        DatabaseReference myReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).child("requests");
        myReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myReference.child(user_id).setValue("DECL");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Update the other user's requests
        DatabaseReference otherReference = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("requests");
        otherReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                otherReference.child(mAuth.getCurrentUser().getUid()).setValue("DECL");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Toast.makeText(getContext(), "Partner Declined", Toast.LENGTH_SHORT).show();
    }
}
