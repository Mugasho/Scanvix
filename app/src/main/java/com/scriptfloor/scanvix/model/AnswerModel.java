package com.scriptfloor.scanvix.model;

import java.util.Date;

/**
 * Created by LINCOLN on 4/6/2019.
 */

public class AnswerModel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHighest() {
        return highest;
    }

    public void setHighest(int highest) {
        this.highest = highest;
    }

    public int getHigher() {
        return higher;
    }

    public void setHigher(int higher) {
        this.higher = higher;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getLowest() {
        return lowest;
    }

    public void setLowest(int lowest) {
        this.lowest = lowest;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public int id;
    public int highest;
    public int higher;
    public int high;
    public int lower;
    public int lowest;
    public String date_added;
}
