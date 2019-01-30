package com.app.court.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.app.court.R;
import com.app.court.entities.SignUpEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.DialogHelper;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsFragment extends BaseFragment {
    @BindView(R.id.btn_changePassword)
    AnyTextView btnChangePassword;
    @BindView(R.id.btn_english)
    AnyTextView btnEnglish;
    @BindView(R.id.btn_Arabic)
    AnyTextView btnArabic;
    @BindView(R.id.toggleNotification)
    ToggleButton toggleNotification;
    @BindView(R.id.btn_about)
    AnyTextView btnAbout;
    @BindView(R.id.btn_terms_condition)
    AnyTextView btnTermsCondition;
    @BindView(R.id.btn_logout)
    AnyTextView btnLogout;
    Unbinder unbinder;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListners();
        if (prefHelper.getSignUpUser().getIsNotify().equals("1")) {
            toggleNotification.setChecked(true);
        } else {
            toggleNotification.setChecked(false);
        }
    }

    private void setListners() {

        toggleNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SignUpEntity signUpEntity=prefHelper.getSignUpUser();
                signUpEntity.setIsNotify(isChecked ? "1" : "0");
                prefHelper.putSignupUser(signUpEntity);
                serviceHelper.enqueueCall(webService.pushOnOff(isChecked ? 1 : 0), WebServiceConstants.PUSHONOFF);
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
            }
        });
        titleBar.setSubHeading(getString(R.string.settings));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_changePassword, R.id.btn_english, R.id.btn_Arabic, R.id.btn_about, R.id.btn_terms_condition, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_changePassword:
                getDockActivity().replaceDockableFragment(ChangePasswordFragment.newInstance(), "ChangePasswordFragment");
                break;
            case R.id.btn_english:
                getMainActivity().notImplemented();
                break;
            case R.id.btn_Arabic:
                getMainActivity().notImplemented();
                break;
            case R.id.btn_about:
                getDockActivity().replaceDockableFragment(AboutFragment.newInstance(), "AboutFragment");
                break;
            case R.id.btn_terms_condition:
                getDockActivity().replaceDockableFragment(TermsAndConditionFragment.newInstance(AppConstants.COMING_FROM_SETTINGS), "TermsAndConditionFragment");
                break;
            case R.id.btn_logout:
                final DialogHelper dialoge = new DialogHelper(getDockActivity());
                dialoge.initlogout(R.layout.logout_dialog, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialoge.hideDialog();
                        logout();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialoge.hideDialog();
                    }
                });
                dialoge.showDialog();
                break;
        }
    }

    private void logout() {
        serviceHelper.enqueueCall(webService.logout(FirebaseInstanceId.getInstance().getToken()), WebServiceConstants.LOGOUT);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.LOGOUT:
                prefHelper.setLoginStatus(false);
                prefHelper.putSignupUser(null);
                prefHelper.setFirebase_TOKEN(null);
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), LoginFragment.class.getName());
                break;

        }
    }
}
