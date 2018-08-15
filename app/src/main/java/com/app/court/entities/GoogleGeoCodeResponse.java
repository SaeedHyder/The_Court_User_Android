package com.app.court.entities;

import com.app.court.entities.address_component;
import com.app.court.entities.geometry;

import java.util.ArrayList;

public class GoogleGeoCodeResponse {

    public String place_id;
    public String formatted_address;
    public geometry geometry;
    public ArrayList<String> types;
    public ArrayList<address_component> address_components;


    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(geometry geometry) {
        this.geometry = geometry;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public ArrayList<address_component> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(ArrayList<address_component> address_components) {
        this.address_components = address_components;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}

