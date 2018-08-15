package com.app.court.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.court.R;
import com.app.court.entities.UserProfileEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.WebServiceConstants;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileFragment extends BaseFragment {
    @BindView(R.id.iv_profile_image)
    CircleImageView ivProfileImage;
    @BindView(R.id.txt_full_name)
    AnyTextView txtFullName;
    @BindView(R.id.txt_email)
    AnyTextView txtEmail;
    @BindView(R.id.txt_phone_number)
    AnyTextView txtPhoneNumber;
    @BindView(R.id.txt_address)
    AnyTextView txtAddress;
    @BindView(R.id.btn_edit_profile)
    Button btnEditProfile;
    private ImageLoader imageLoader;
    Unbinder unbinder;

    public static MyProfileFragment newInstance() {
        Bundle args = new Bundle();

        MyProfileFragment fragment = new MyProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setProfileDataFromService();
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
        titleBar.setSubHeading(getString(R.string.profile));
    }

    private void setProfileDataFromPreferences() {

        if (prefHelper != null && prefHelper.getSignUpUser() != null) {
            if (prefHelper.getSignUpUser().getImageUrl() != null)
                txtFullName.setText(prefHelper.getSignUpUser().getImageUrl() + "");
            if (prefHelper.getSignUpUser().getFullName() != null)
                txtFullName.setText(prefHelper.getSignUpUser().getFullName() + "");
            if (prefHelper.getSignUpUser().getEmail() != null)
                txtEmail.setText(prefHelper.getSignUpUser().getEmail() + "");
            if (prefHelper.getSignUpUser().getPhoneNo() != null)
                txtPhoneNumber.setText(prefHelper.getSignUpUser().getPhoneNo() + "");
            if (prefHelper.getSignUpUser().getLocation() != null)
                txtAddress.setText(prefHelper.getSignUpUser().getLocation() + "");
        }
    }

    private void setProfileDataFromService() {
        serviceHelper.enqueueCall(webService.getProfile(), WebServiceConstants.GET_PROFILE);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_PROFILE:
                UserProfileEntity entity = (UserProfileEntity) result;
                prefHelper.putUserProfile(entity);
                if (entity != null) {
                    if (entity.getImageUrl() != null)
                        imageLoader.displayImage(entity.getImageUrl() , ivProfileImage);
                    if (entity.getFullName() != null)
                        txtFullName.setText(entity.getFullName() + "");
                    if (entity.getEmail() != null)
                        txtEmail.setText(entity.getEmail() + "");
                    if (entity.getPhoneNo() != null)
                        txtPhoneNumber.setText(entity.getPhoneNo() + "");
                    if (entity.getLocation() != null)
                        txtAddress.setText(entity.getLocation() + "");

                }
                break;
        }
    }

    @OnClick(R.id.btn_edit_profile)
    public void onViewClicked() {
        getDockActivity().popFragment();
        getDockActivity().replaceDockableFragment(EditProfileFragment.newInstance(), "EditProfileFragment");
    }
}
