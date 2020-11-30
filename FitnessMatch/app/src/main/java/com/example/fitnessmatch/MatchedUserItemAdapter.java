package com.example.fitnessmatch;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        tv_user_name.setText(name);
        tv_distance.setText(distance + "mi");
        tv_match_score.setText(match_score + "% match");

        int int_match_score = Integer.parseInt(match_score);

        if (int_match_score <  100 && int_match_score >=  80){
            tv_match_score.setTextColor(mContext.getColor(R.color.green));
        }
        else if (int_match_score <  80 && int_match_score >=  40){
            tv_match_score.setTextColor(mContext.getColor(R.color.orange));
        }

        else if (int_match_score <  40 && int_match_score >=  0){
            tv_match_score.setTextColor(mContext.getColor(R.color.red));
        }

        return convertView;

    }
}