package com.app.court.global;

public enum SideMenuChooser {

    DRAWER("1"),
    RESIDE_MENU("2");

    private final String sideMenu;

    SideMenuChooser(String sideMenu) {
        this.sideMenu = sideMenu;
    }

    public String getValue() {
        return this.sideMenu;
    }

}
