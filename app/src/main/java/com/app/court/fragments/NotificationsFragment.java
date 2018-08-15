package com.app.court.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.app.court.R;
import com.app.court.entities.EntityCms;
import com.app.court.entities.NotificationEnt;
import com.app.court.entities.NotificationEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.global.WebServiceConstants;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.binders.BinderNotification;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotificationsFragment extends BaseFragment {

    @BindView(R.id.lv_notification)
    ListView lvNotification;
    Unbinder unbinder;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayListAdapter<NotificationEntity> adapter;
    private ArrayList<NotificationEntity> getNotifications=new ArrayList<>();


    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderNotification(getDockActivity(), prefHelper));
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.notification));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getNotifications();
    }

    private void getNotifications() {
        serviceHelper.enqueueCall(webService.getNotifications(), WebServiceConstants.GET_NOTIFICATIONS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_NOTIFICATIONS:
                getNotifications = (ArrayList<NotificationEntity>) result;
                if (getNotifications.size() > 0) {
                    lvNotification.setVisibility(View.VISIBLE);
                    txtNoData.setVisibility(View.GONE);
                } else {
                    lvNotification.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                }
                bindData(getNotifications);
                break;
        }
    }


    private void bindData(ArrayList<NotificationEntity> notificationCollection) {

        adapter.clearList();
        lvNotification.setAdapter(adapter);
        adapter.addAll(notificationCollection);

    }
}
