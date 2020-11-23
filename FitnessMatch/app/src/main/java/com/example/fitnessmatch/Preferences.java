package com.example.fitnessmatch;

import java.io.Serializable;

public class Preferences implements Serializable{

    private boolean hiking, walking, yoga, pilates, weight_lifting, biking, circuit_training, swimming, basketball, volleyball, soccer, ultimate_frisbee, running;
    private int frequency, pref_time, level;
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
        this.other_activity = "";
    }

    public Preferences(boolean hiking, boolean walking, boolean yoga, boolean pilates, boolean weight_lifting, boolean biking, boolean circuit_training, boolean swimming, boolean basketball, boolean volleyball, boolean soccer, boolean ultimate_frisbee, boolean running, int frequency, int pref_time, int level, String other_activity) {
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
        this.other_activity = other_activity;
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
}
