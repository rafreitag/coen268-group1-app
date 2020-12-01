package com.example.fitnessmatch;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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

public class MatchedUserItemAdapter extends ArrayAdapter<MatchedUserItem> {
    private Context mContext;
    private int mResource;

    public MatchedUserItemAdapter(Context context, int resource,  @NonNull ArrayList<MatchedUserItem> objects) {
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
        Button btn_send_request = (Button) convertView.findViewById(R.id.btn_send_req);

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
        btn_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequestTo(user_id);
                //verification button
                //change color and stuff
                btn_send_request.setBackgroundTintList(mContext.getColorStateList(R.color.lightGray));
                //toast?
            }
        });



        return convertView;

    }

    //can make request handler class later to hold all request stuff
    public void sendRequestTo(String receiver){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String TAG = "sendRequestTo";
        Log.i(TAG, "request sent from user " + mAuth.getCurrentUser().getUid() + " to user " + receiver);

        DatabaseReference senderReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).child("requests");
        senderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderReference.child(receiver).setValue("SENT");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference receiverReference = FirebaseDatabase.getInstance().getReference("users").child(receiver).child("requests");
        receiverReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiverReference.child(mAuth.getCurrentUser().getUid()).setValue("RECV");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}