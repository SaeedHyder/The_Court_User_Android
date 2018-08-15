package com.app.court.entities;



public class geometry {

    public bounds bounds;
    public String location_type ;
    public location location;
    public bounds viewport;

    public bounds getBounds() {
        return bounds;
    }

    public void setBounds(bounds bounds) {
        this.bounds = bounds;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }


    public location getLocation() {
        return location;
    }

    public void setLocation(location location) {
        this.location = location;
    }

    public bounds getViewport() {
        return viewport;
    }

    public void setViewport(bounds viewport) {
        this.viewport = viewport;
    }
}
