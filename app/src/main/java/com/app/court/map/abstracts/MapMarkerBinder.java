package com.app.court.map.abstracts;

import com.google.android.gms.maps.GoogleMap;

public abstract class MapMarkerBinder<T> {

    public abstract void addMarker(GoogleMap googleMap,T entity,int position);
}
