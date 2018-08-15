package com.app.court.entities;


public class FilterCaseEnt {

    String Tag;
    int Status;
    boolean isChecked=false;

    public FilterCaseEnt(String tag, int status, boolean isChecked) {
        Tag = tag;
        Status = status;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
