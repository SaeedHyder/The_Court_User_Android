package com.app.court.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.app.court.R;
import com.app.court.entities.MediaEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.binders.BinderDisplayMedia;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DisplayMediaFragment extends BaseFragment {

    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.gv_gallery)
    GridView gvGallery;
    Unbinder unbinder;
    private ArrayList<String> mediaCollection;
    private ArrayListAdapter<MediaEntity> adapter;
    private static String TYPE;
    private static ArrayList<MediaEntity> IMAGES;

    public static DisplayMediaFragment newInstance(String type, ArrayList<MediaEntity> images) {
        Bundle args = new Bundle();
        TYPE = type;
        IMAGES = images;
        DisplayMediaFragment fragment = new DisplayMediaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DisplayMediaFragment newInstance(String type) {
        Bundle args = new Bundle();
        TYPE = type;
        DisplayMediaFragment fragment = new DisplayMediaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderDisplayMedia(getDockActivity(),prefHelper));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_media, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindData(IMAGES);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(TYPE);
    }

    public void bindData(ArrayList<MediaEntity> images) {

        if (IMAGES != null) {
            if (IMAGES.size() <= 0) {
                txtNoData.setVisibility(View.VISIBLE);
                gvGallery.setVisibility(View.GONE);
            } else {
                txtNoData.setVisibility(View.GONE);
                gvGallery.setVisibility(View.VISIBLE);

                adapter.clearList();
                gvGallery.setAdapter(adapter);
                adapter.addAll(IMAGES);
            }
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            gvGallery.setVisibility(View.GONE);
        }
    }


}
