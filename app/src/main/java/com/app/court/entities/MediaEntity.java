package com.app.court.entities;

public class MediaEntity {

    String photo;
    String name;
    String type;
    String date;

    public MediaEntity(String photo, String name, String type, String date) {
        this.photo = photo;
        this.name = name;
        this.type = type;
        this.date = date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
