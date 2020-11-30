package com.example.fitnessmatch;

public class MatchedUserItem {
    private String user_id;
    private String name;
    private String distance;
    private String match_score;

    public MatchedUserItem(String user_id, String name, String distance, String match_score) {
        this.user_id = user_id;
        this.name = name;
        this.distance = distance;
        this.match_score = match_score;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }

    public String getMatch_score() {
        return match_score;
    }
}
