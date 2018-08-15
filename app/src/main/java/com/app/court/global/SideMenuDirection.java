package com.app.court.global;

public enum SideMenuDirection {

    LEFT("3"),
    RIGHT("5");

    private final String direction;

    SideMenuDirection(String direction) {
        this.direction = direction;
    }

    public String getValue() {
        return this.direction;
    }
}
