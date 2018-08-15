package com.app.court.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Adapter_ResourcePager extends PagerAdapter {


    private ArrayList<Integer> mResources;
    private Context mActivity;
    private int resId = 0;
    private LayoutInflater li;

    public Adapter_ResourcePager(Context activity, ArrayList<Integer> resources) {
        mResources = resources;
        mActivity = activity;
        li = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public Object instantiateItem(ViewGroup parent, int position) {

        switch (position) {
            case 0:
                resId = mResources.get(0);
                break;
            case 1:
                resId = mResources.get(1);
                break;
            case 2:
                resId = mResources.get(2);
                break;
            case 3:
                resId = mResources.get(3);
                break;


        }
        View view = null;
        try {
            view = (View) li.inflate(resId, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
        lp.width = ViewPager.LayoutParams.MATCH_PARENT;
        lp.height = ViewPager.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(lp);
        parent.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup parent, int position, Object object) {
        View view = (View) object;
        parent.removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }
}
