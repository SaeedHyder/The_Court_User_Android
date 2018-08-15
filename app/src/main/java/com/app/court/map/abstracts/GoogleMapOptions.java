package com.app.court.map.abstracts;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class GoogleMapOptions<T> implements GoogleMap.InfoWindowAdapter,GoogleMap.OnInfoWindowClickListener {

    private Activity activity;
    private GoogleMap googleMap;
    private ArrayList<T> markerCollection;
    private InfoWindowBinder<T> infoWindowBinder;
    MapMarkerBinder<T> mapMarkerBinder;

    public GoogleMapOptions(Activity activity,GoogleMap googleMap, ArrayList<T> markerCollection, InfoWindowBinder<T> infoWindowBinder, MapMarkerBinder<T> mapMarkerBinder) {
        this.activity = activity;
        this.googleMap = googleMap;
        this.markerCollection = markerCollection;
        this.infoWindowBinder = infoWindowBinder;
        this.mapMarkerBinder = mapMarkerBinder;
    }

    public void addMarkers(){
        for (int i =0; i<markerCollection.size() ; i++){
            mapMarkerBinder.addMarker(googleMap,markerCollection.get(i),i);
        }
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        try {
            View view = infoWindowBinder.showView(activity);
            infoWindowBinder.bindView(googleMap, markerCollection.get(Integer.valueOf(marker.getTitle())), Integer.valueOf(marker.getTitle()), view, activity);

            return view;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        infoWindowBinder.openBalloonView(markerCollection.get(Integer.valueOf(marker.getTitle())), Integer.valueOf(marker.getTitle()), activity);

    }
}
