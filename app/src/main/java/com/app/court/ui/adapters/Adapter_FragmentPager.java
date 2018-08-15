package com.app.court.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.court.fragments.abstracts.BaseFragment;

import java.util.ArrayList;

public class Adapter_FragmentPager extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> mFragmentCollection;
    private FragmentManager fm;


    public Adapter_FragmentPager(FragmentManager fm, ArrayList<BaseFragment> fragmentCollection) {
        super(fm);
        this.fm = fm;
        mFragmentCollection =  fragmentCollection;
    }

    @Override
    public Fragment getItem(int position) {

       return mFragmentCollection.get(position);



    }

    @Override
    public int getCount() {
        return mFragmentCollection.size();
    }






}
