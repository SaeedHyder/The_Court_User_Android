package com.app.court.entities;

public class EntityMessageThread {

    private String image;
    private String name;
    private String description;
    private String date;
    private String time;

    public EntityMessageThread(String image, String name, String description, String date, String time) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
