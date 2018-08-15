package com.app.court.entities;

public class FilterEnt {

    String data;
    String id;
    boolean isChecked=false;

    public FilterEnt(String data,String id ) {
        this.data = data;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
