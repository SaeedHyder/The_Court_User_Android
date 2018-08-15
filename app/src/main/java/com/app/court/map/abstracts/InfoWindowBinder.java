package com.app.court.map.abstracts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;

public abstract class InfoWindowBinder<T> {

    private int resId;

    public InfoWindowBinder(int resId){
        this.resId = resId;
    }

    public View showView(Activity activity){
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate( resId , null );
        return view;


    }

    public abstract void bindView(GoogleMap googleMap,T entity,int position,View vew,Activity activity);
    public abstract void openBalloonView(T entity, int position, Activity activity);
}
