package com.app.court.entities;

public class EntityChat {

    private String left_name;
    private String left_msg;
    private String left_time;
    private String left_date;
    private String right_name;
    private String right_msg;
    private String right_time;
    private String right_date;

    public EntityChat(String left_name, String left_msg, String left_time, String left_date, String right_name, String right_msg, String right_time, String right_date) {
        this.left_name = left_name;
        this.left_msg = left_msg;
        this.left_time = left_time;
        this.left_date = left_date;
        this.right_name = right_name;
        this.right_msg = right_msg;
        this.right_time = right_time;
        this.right_date = right_date;
    }

    public String getLeft_name() {
        return left_name;
    }

    public void setLeft_name(String left_name) {
        this.left_name = left_name;
    }

    public String getLeft_msg() {
        return left_msg;
    }

    public void setLeft_msg(String left_msg) {
        this.left_msg = left_msg;
    }

    public String getLeft_time() {
        return left_time;
    }

    public void setLeft_time(String left_time) {
        this.left_time = left_time;
    }

    public String getLeft_date() {
        return left_date;
    }

    public void setLeft_date(String left_date) {
        this.left_date = left_date;
    }

    public String getRight_name() {
        return right_name;
    }

    public void setRight_name(String right_name) {
        this.right_name = right_name;
    }

    public String getRight_msg() {
        return right_msg;
    }

    public void setRight_msg(String right_msg) {
        this.right_msg = right_msg;
    }

    public String getRight_time() {
        return right_time;
    }

    public void setRight_time(String right_time) {
        this.right_time = right_time;
    }

    public String getRight_date() {
        return right_date;
    }

    public void setRight_date(String right_date) {
        this.right_date = right_date;
    }
}
