package com.app.court.entities;

import com.app.court.helpers.DateHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saeedhyder on 9/15/2017.
 */

public class NotificationEnt {

    String text;
    String date;

    public NotificationEnt(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}