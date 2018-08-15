package com.app.court.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.court.R;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.ui.views.TitleBar;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.court.global.WebServiceConstants.UPDATE_DEVICE_TOKEN;


public class HomeFragment extends BaseFragment {


    @BindView(R.id.iv_notification)
    ImageView ivNotification;
    @BindView(R.id.btn_find_laywer)
    LinearLayout btnFindLaywer;
    @BindView(R.id.btn_my_case)
    LinearLayout btnMyCase;
    @BindView(R.id.btn_library)
    LinearLayout btnLibrary;
    @BindView(R.id.btn_messages)
    LinearLayout btnMessages;
    @BindView(R.id.btn_my_profile)
    LinearLayout btnMyProfile;
    @BindView(R.id.btn_settings)
    LinearLayout btnSettings;
    Unbinder unbinder;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (prefHelper.getFirebase_TOKEN() == null || prefHelper.getFirebase_TOKEN().equals("")) {
            serviceHelper.enqueueCall(webService.updateDeviceToken(FirebaseInstanceId.getInstance().getToken(), AppConstants.Device_Type), UPDATE_DEVICE_TOKEN);
        }

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);
        switch (Tag) {
            case UPDATE_DEVICE_TOKEN:
                prefHelper.setFirebase_TOKEN(FirebaseInstanceId.getInstance().getToken());
                break;
        }
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @OnClick({R.id.iv_notification, R.id.btn_find_laywer, R.id.btn_my_case, R.id.btn_library, R.id.btn_messages, R.id.btn_my_profile, R.id.btn_settings})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_notification:
                getDockActivity().replaceDockableFragment(NotificationsFragment.newInstance(),"NotificationsFragment");
                break;
            case R.id.btn_find_laywer:
                getDockActivity().replaceDockableFragment(FindLawyerFragment.newInstance(),"FindLawyerFragment");
                break;
            case R.id.btn_my_case:
                getDockActivity().replaceDockableFragment(MyCaseFragment.newInstance(),"MyCaseFragment");
                break;
            case R.id.btn_library:
                getDockActivity().replaceDockableFragment(LibraryFragment.newInstance(),"LibraryFragment");
                break;
            case R.id.btn_messages:
                getDockActivity().replaceDockableFragment(MessagesThreadFragment.newInstance(),"MessagesThreadFragment");
                break;
            case R.id.btn_my_profile:
                getDockActivity().replaceDockableFragment(MyProfileFragment.newInstance(),"MyProfileFragment");
                break;
            case R.id.btn_settings:
                getDockActivity().replaceDockableFragment(SettingsFragment.newInstance(),"SettingsFragment");
                break;
        }
    }
}

