package com.app.court.entities;

public class bounds {

    public location northeast ;
    public location southwest ;

    public location getNortheast() {
        return northeast;
    }

    public void setNortheast(location northeast) {
        this.northeast = northeast;
    }

    public location getSouthwest() {
        return southwest;
    }

    public void setSouthwest(location southwest) {
        this.southwest = southwest;
    }
}
