package com.app.court.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.app.court.R;
import com.app.court.entities.SignUpEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.global.WebServiceConstants;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ForgotPasswordFragment extends BaseFragment {


    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.edt_email)
    AnyEditTextView edtEmail;
    @BindView(R.id.btn_login)
    Button btnLogin;
    Unbinder unbinder;

    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
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
        return edtEmail.testValidity();
    }

    @OnClick({R.id.back_btn, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                getMainActivity().popFragment();
                break;
            case R.id.btn_login:
                if (validate())
                    forgotPassword();
                break;
        }
    }

    private void forgotPassword() {
        serviceHelper.enqueueCall(webService.forgotPassword(edtEmail.getText().toString() + ""), WebServiceConstants.FORGOT_PASSWORD);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.FORGOT_PASSWORD:
                SignUpEntity entity=(SignUpEntity)result;
                prefHelper.putSignupUser(entity);
                getDockActivity().replaceDockableFragment(EnterCodeFragment.newInstance(AppConstants.COMING_FROM_FORGOT_PASSWORD), "EnterCodeFragment");
                break;
        }
    }
}
