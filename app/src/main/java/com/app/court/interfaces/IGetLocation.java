package com.app.court.interfaces;

import com.google.android.gms.maps.model.LatLng;

public interface IGetLocation {

    public void onLocationSet(LatLng location, String formattedAddress);
}