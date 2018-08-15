package com.app.court.fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.app.court.R;
import com.app.court.activities.MainActivity;
import com.app.court.entities.ResponseWrapper;
import com.app.court.entities.UserProfileEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.CameraHelper;
import com.app.court.helpers.InternetHelper;
import com.app.court.helpers.UIHelper;
import com.app.court.interfaces.IGetLocation;
import com.app.court.interfaces.ImageSetter;
import com.app.court.ui.dialogs.DialogFactory;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends BaseFragment implements MainActivity.ImageSetter, IGetLocation, View.OnClickListener {
    @BindView(R.id.circleImageView2)
    CircleImageView circleImageView2;
    @BindView(R.id.iv_profile_image)
    CircleImageView ivProfileImage;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.txt_full_name)
    AnyEditTextView txtFullName;
    @BindView(R.id.txt_email)
    AnyEditTextView txtEmail;
    @BindView(R.id.txt_phone_number)
    AnyEditTextView txtPhoneNumber;
    @BindView(R.id.txt_address)
    AnyTextView txtAddress;
    @BindView(R.id.btn_done)
    Button btnDone;
    String imagePath;
    private LatLng location;
    private String lat;
    private String longi;
    Unbinder unbinder;

    private File profilePic;
    private String profilePath;
    private ImageLoader imageLoader;

    public static EditProfileFragment newInstance() {
        Bundle args = new Bundle();

        EditProfileFragment fragment = new EditProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        getMainActivity().setImageSetter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtAddress.setOnClickListener(this);
        setProfileDataFromPreferences();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.profile));
    }

    private boolean isValidated() {

        if (txtFullName.getText() == null || (txtFullName.getText().toString().isEmpty())) {
            if (txtFullName.requestFocus()) {
                setEditTextFocus(txtFullName);
            }
            txtFullName.setError(getString(R.string.enter_FullName));
            return false;
        } else if (txtEmail.getText() == null || (txtEmail.getText().toString().isEmpty()) ||
                (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches())) {
            if (txtEmail.requestFocus()) {
                setEditTextFocus(txtEmail);
            }
            txtEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (txtPhoneNumber.getText() == null || (txtPhoneNumber.getText().toString().isEmpty())) {
            if (txtPhoneNumber.requestFocus()) {
                setEditTextFocus(txtPhoneNumber);
            }
            txtPhoneNumber.setError(getString(R.string.enter_MobileNum));
            return false;
        } /*else if (txtAddress.getText() == null || (txtAddress.getText().toString().isEmpty())) {
            if (txtAddress.requestFocus()) {
                setEditTextFocus(txtAddress);
            }
            txtAddress.setError(getString(R.string.enter_address));
            return false;
        }*/ else {
            return true;
        }
    }

    private void setProfileDataFromPreferences() {

        if (prefHelper != null && prefHelper.getUserProfile() != null) {
            if (prefHelper.getUserProfile().getImageUrl() != null)
                imageLoader.displayImage(prefHelper.getUserProfile().getImageUrl(), ivProfileImage);
            if (prefHelper.getUserProfile().getFullName() != null)
                txtFullName.setText(prefHelper.getUserProfile().getFullName() + "");
            if (prefHelper.getUserProfile().getEmail() != null)
                txtEmail.setText(prefHelper.getUserProfile().getEmail() + "");
            if (prefHelper.getUserProfile().getPhoneNo() != null)
                txtPhoneNumber.setText(prefHelper.getUserProfile().getPhoneNo() + "");
            if (prefHelper.getUserProfile().getLocation() != null)
                txtAddress.setText(prefHelper.getUserProfile().getLocation() + "");
            if (prefHelper.getUserProfile().getLatitude() != null)
                lat = prefHelper.getUserProfile().getLatitude() + "";
            if (prefHelper.getUserProfile().getLongitude() != null)
                longi = prefHelper.getUserProfile().getLongitude() + "";

        }
    }

    @OnClick({R.id.iv_camera, R.id.btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_camera:
                CameraHelper.uploadPhotoDialog(getMainActivity());
                break;
            case R.id.btn_done:
                if (isValidated())
                    if (lat != null && longi != null)
                        updateProfile();
                break;
        }
    }


    @Override
    public void setImage(String imagePath, String thumbnail) {
        if (imagePath != null) {

            try {
                profilePic = new Compressor(getDockActivity()).compressToFile(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            profilePath = imagePath;
            imageLoader.displayImage("file:///" + imagePath, ivProfileImage);
        }
    }

    @Override
    public void setFilePath(String filePath) {
    }

    @Override
    public void setVideo(String filePath, String extension, String thumbnail) {
    }


    @Override
    public void onClick(View v) {
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            MapControllerFragment mapControllerFragment = MapControllerFragment.newInstance();
            mapControllerFragment.setDelegate(this);
            DialogFactory.showMapControllerDialog(getDockActivity(), mapControllerFragment);
        }
    }

    @Override
    public void onLocationSet(LatLng location, String formattedAddress) {
        txtAddress.setText(formattedAddress + "");
        this.location = location;
        lat = String.valueOf(location.latitude);
        longi = String.valueOf(location.longitude);
        UIHelper.hideSoftKeyboard(getDockActivity(),getView());
    }

    private void updateProfile() {

        MultipartBody.Part filePart = null;
        if (profilePic != null) {
            filePart = MultipartBody.Part.createFormData("profile_image", profilePic.getName(), RequestBody.create(MediaType.parse("image"), profilePic));
        }

        loadingStarted();
        Call<ResponseWrapper<UserProfileEntity>> call = webService.updateUserProfile(
                RequestBody.create(MediaType.parse("text/plain"), txtFullName.getText().toString() + ""),
                RequestBody.create(MediaType.parse("text/plain"), txtEmail.getText().toString() + ""),
                RequestBody.create(MediaType.parse("text/plain"), txtPhoneNumber.getText().toString() + ""),
                RequestBody.create(MediaType.parse("text/plain"), txtAddress.getText().toString() + ""),
                RequestBody.create(MediaType.parse("text/plain"), lat),
                RequestBody.create(MediaType.parse("text/plain"), longi),
                filePart != null ? filePart : null);

        call.enqueue(new Callback<ResponseWrapper<UserProfileEntity>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<UserProfileEntity>> call, Response<ResponseWrapper<UserProfileEntity>> response) {
                loadingFinished();


                if (response.body().getCode().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    prefHelper.putUserProfile(response.body().getResult());
                    UIHelper.showLongToastInCenter(getDockActivity(), "Profile updated successfully!");
                    getDockActivity().popFragment();
                    getDockActivity().replaceDockableFragment(MyProfileFragment.newInstance(), "MyProfileFragment");
                } else {
                    UIHelper.showLongToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<UserProfileEntity>> call, Throwable t) {
                loadingFinished();
                t.printStackTrace();
            }
        });
    }
}
