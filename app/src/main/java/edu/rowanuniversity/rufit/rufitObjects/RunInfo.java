package edu.rowanuniversity.rufit.rufitObjects;

import java.util.Date;

/**
 * Created by Naomi on 3/28/2017.
 */

public class RunInfo {

    private float distance, pace;
    // calories
    // time measured in seconds
    private int time, shoeID, runID;
    private String userID, notes;
    private int feel;
    private String type;
    private String date;



    public RunInfo(float distance, float pace, int time, String date, int feel, String type, String notes) {
        this.distance = distance;
        this.pace = pace;
        this.time = time;
        this.date = date;
        this.feel = feel;
        this.type = type;
        this.notes = notes;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getPace() {
        return pace;
    }

    public void calculatePace() {
        pace = time/distance;
    }

    public int getShoeID() {
        return shoeID;
    }

    public void setShoeID(int shoeID) {
        this.shoeID = shoeID;
    }
}
