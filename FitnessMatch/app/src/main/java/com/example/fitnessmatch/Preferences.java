package com.example.fitnessmatch;

import java.io.Serializable;

public class Preferences implements Serializable{

    private boolean hiking, walking, yoga, pilates, weight_lifting, biking, circuit_training, swimming, basketball, volleyball, soccer, ultimate_frisbee, running;
    private int frequency, pref_time, level;
    private double latitude, longitude;
    private String other_activity;

    public Preferences(){
        this.hiking = false;
        this.walking = false;
        this.yoga = false;
        this.pilates = false;
        this.weight_lifting = false;
        this.biking = false;
        this.circuit_training = false;
        this.swimming = false;
        this.basketball = false;
        this.volleyball = false;
        this.soccer = false;
        this.ultimate_frisbee = false;
        this.running = false;
        this.frequency = 0;
        this.pref_time = 0;
        this.level = 0;
        this.latitude = 0;
        this.longitude = 0;
        this.other_activity = "";
    }

    public Preferences(boolean hiking, boolean walking, boolean yoga, boolean pilates, boolean weight_lifting, boolean biking, boolean circuit_training, boolean swimming, boolean basketball, boolean volleyball, boolean soccer, boolean ultimate_frisbee, boolean running, int frequency, int pref_time, int level, double latitude, double longitude, String other_activity) {
        this.hiking = hiking;
        this.walking = walking;
        this.yoga = yoga;
        this.pilates = pilates;
        this.weight_lifting = weight_lifting;
        this.biking = biking;
        this.circuit_training = circuit_training;
        this.swimming = swimming;
        this.basketball = basketball;
        this.volleyball = volleyball;
        this.soccer = soccer;
        this.ultimate_frisbee = ultimate_frisbee;
        this.running = running;
        this.frequency = frequency;
        this.pref_time = pref_time;
        this.level = level;
        this.latitude = latitude;
        this.longitude = longitude;
        this.other_activity = other_activity;
    }

    public String printData(){
        String s = "Hiking: " + Boolean.toString(this.isHiking()) +
                "Walking: " + Boolean.toString(this.isBiking()) +
                "Other: " + getOther_activity();

        return s;
    }

    public boolean isHiking() {
        return hiking;
    }

    public void setHiking(boolean hiking) {
        this.hiking = hiking;
    }

    public boolean isWalking() {
        return walking;
    }

    public void setWalking(boolean walking) {
        this.walking = walking;
    }

    public boolean isYoga() {
        return yoga;
    }

    public void setYoga(boolean yoga) {
        this.yoga = yoga;
    }

    public boolean isPilates() {
        return pilates;
    }

    public void setPilates(boolean pilates) {
        this.pilates = pilates;
    }

    public boolean isWeight_lifting() {
        return weight_lifting;
    }

    public void setWeight_lifting(boolean weight_lifting) {
        this.weight_lifting = weight_lifting;
    }

    public boolean isBiking() {
        return biking;
    }

    public void setBiking(boolean biking) {
        this.biking = biking;
    }

    public boolean isCircuit_training() {
        return circuit_training;
    }

    public void setCircuit_training(boolean circuit_training) {
        this.circuit_training = circuit_training;
    }

    public boolean isSwimming() {
        return swimming;
    }

    public void setSwimming(boolean swimming) {
        this.swimming = swimming;
    }

    public boolean isBasketball() {
        return basketball;
    }

    public void setBasketball(boolean basketball) {
        this.basketball = basketball;
    }

    public boolean isVolleyball() {
        return volleyball;
    }

    public void setVolleyball(boolean volleyball) {
        this.volleyball = volleyball;
    }

    public boolean isSoccer() {
        return soccer;
    }

    public void setSoccer(boolean soccer) {
        this.soccer = soccer;
    }

    public boolean isUltimate_frisbee() {
        return ultimate_frisbee;
    }

    public void setUltimate_frisbee(boolean ultimate_frisbee) {
        this.ultimate_frisbee = ultimate_frisbee;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getPref_time() {
        return pref_time;
    }

    public void setPref_time(int pref_time) {
        this.pref_time = pref_time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOther_activity() {
        return other_activity;
    }

    public void setOther_activity(String other_activity) {
        this.other_activity = other_activity;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int calculateMatchScore(Preferences p) {
        int count = 0;

        if (this.isHiking() == p.isHiking()) { count++; }
        if (this.isYoga() == p.isYoga()) { count++; }
        if (this.isPilates() == p.isPilates()) { count++; }
        if (this.isWeight_lifting() == p.isWeight_lifting()) { count++; }
        if (this.isBiking() == p.isBiking()) { count++; }
        if (this.isCircuit_training() == p.isCircuit_training()) { count++; }
        if (this.isBasketball() == p.isBasketball()) { count++; }
        if (this.isVolleyball() == p.isVolleyball()) { count++; }
        if (this.isSoccer() == p.isSoccer()) { count++; }
        if (this.isUltimate_frisbee() == p.isUltimate_frisbee()) { count++; }
        if (this.isRunning() == p.isRunning()) { count++; }
        if (this.getFrequency() == p.getFrequency()) { count++; }
        if (this.getPref_time() == p.getPref_time()) { count++; }
        if (this.getLevel() == p.getLevel()) { count++; }

        double score = (double) count / 14;

        return (int) Math.floor(score * 100);

    }

    public double calculateDistanceFrom(Preferences p) {
        double theta = this.getLongitude() - p.getLongitude();
        double dist = Math.sin(deg2rad(this.getLatitude()))
                * Math.sin(deg2rad(p.getLatitude()))
                + Math.cos(deg2rad(this.getLatitude()))
                * Math.cos(deg2rad(p.getLatitude()))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
