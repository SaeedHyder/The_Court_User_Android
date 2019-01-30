package com.app.court.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.app.court.R;
import com.app.court.entities.SignUpEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignupFragment extends BaseFragment {


    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.edt_name)
    AnyEditTextView edtName;
    @BindView(R.id.edt_email)
    AnyEditTextView edtEmail;
    @BindView(R.id.edt_phone_number)
    AnyEditTextView edtPhoneNumber;
    @BindView(R.id.edt_password)
    AnyEditTextView edtPassword;
    @BindView(R.id.edt_cfm_password)
    AnyEditTextView edtCfmPassword;
    @BindView(R.id.btn_signup)
    Button btnSignup;
    @BindView(R.id.tv_signin)
    AnyTextView tvSignin;
    Unbinder unbinder;

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }


    private boolean validate() {
        return edtName.testValidity() && edtEmail.testValidity() && edtPhoneNumber.testValidity()
                && edtPassword.testValidity() && edtCfmPassword.testValidity();
    }

    @OnClick({R.id.back_btn, R.id.btn_signup, R.id.tv_signin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                getMainActivity().popFragment();
                break;
            case R.id.btn_signup:
                if (validate())
                    if (edtPhoneNumber.getText().toString().length() <7) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.phone_length_alert));
                    } else if (edtPassword.getText().toString().length() < 6) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_length_alert));
                    } else if (edtCfmPassword.getText().toString().length() < 6) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_length_alert));
                    } else if (!edtPassword.getText().toString().equalsIgnoreCase(edtCfmPassword.getText().toString())) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.conform_password_error));
                    } else {
                        signUp();
                    }
                break;
            case R.id.tv_signin:
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                break;
        }
    }

    private void signUp() {
        serviceHelper.enqueueCall(webService.signup(edtName.getText().toString() + "", edtEmail.getText().toString() + "", edtPhoneNumber.getText().toString() + "", edtPassword.getText().toString() + "",edtPassword.getText().toString() + "", AppConstants.DEVICE_TYPE, FirebaseInstanceId.getInstance().getToken(), ""), WebServiceConstants.SIGN_UP);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.SIGN_UP:
                SignUpEntity entity = (SignUpEntity) result;
                prefHelper.putSignupUser(entity);
                getMainActivity().popBackStackTillEntry(1);
                getDockActivity().replaceDockableFragment(TermsAndConditionFragment.newInstance(AppConstants.COMING_FROM_SIGNUP), "TermsAndConditionFragment");
                break;
        }
    }
}
