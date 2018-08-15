package com.app.court.fragments;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

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


public class LoginFragment extends BaseFragment {

    @BindView(R.id.edt_email)
    AnyEditTextView edtEmail;
    @BindView(R.id.edt_password)
    AnyEditTextView edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forgot_password)
    AnyTextView tvForgotPassword;
    @BindView(R.id.tv_signup)
    AnyTextView tvSignup;
    Unbinder unbinder;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        SpannableString content = new SpannableString("Forgot Password?");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvForgotPassword.setText(content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    private boolean validate() {
        return edtEmail.testValidity() && edtPassword.testValidity();
    }

    @OnClick({R.id.btn_login, R.id.tv_forgot_password, R.id.tv_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (validate())
                    if (edtPassword.getText().toString().length() < 6) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_length_alert));
                    } else {
                        login();
                    }
                break;
            case R.id.tv_forgot_password:
                getDockActivity().replaceDockableFragment(ForgotPasswordFragment.newInstance(), "ForgotPasswordFragment");
                break;
            case R.id.tv_signup:
                getDockActivity().replaceDockableFragment(SignupFragment.newInstance(), "SignupFragment");
                break;
        }
    }

    private void login() {
        serviceHelper.enqueueCall(webService.login(edtEmail.getText().toString() + "", edtPassword.getText().toString() + "", FirebaseInstanceId.getInstance().getToken(), AppConstants.DEVICE_TYPE), WebServiceConstants.LOGIN);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.LOGIN:
                prefHelper.setLoginStatus(true);
                getDockActivity().popBackStackTillEntry(0);
                SignUpEntity entity = (SignUpEntity) result;
                prefHelper.putSignupUser(entity);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                break;
        }
    }
}
