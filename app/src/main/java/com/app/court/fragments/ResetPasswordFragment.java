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
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ResetPasswordFragment extends BaseFragment {

    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.edt_password)
    AnyEditTextView edtPassword;
    @BindView(R.id.edt_cfm_password)
    AnyEditTextView edtCfmPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    Unbinder unbinder;

    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private boolean validate() {
        return edtPassword.testValidity() && edtCfmPassword.testValidity();
    }

    @OnClick({R.id.back_btn, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                getMainActivity().popFragment();
                break;
            case R.id.btn_login:
                if (validate())
                    if (edtPassword.getText().toString().length() < 6) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_length_alert));
                    } else if (edtCfmPassword.getText().toString().length() < 6) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_length_alert));
                    } else if (!edtPassword.getText().toString().equalsIgnoreCase(edtCfmPassword.getText().toString())) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.password_not_match));
                    } else {
                        resetPassword();
                    }
                break;
        }
    }

    private void resetPassword() {
        if (prefHelper != null && prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getId() != null)
            serviceHelper.enqueueCall(webService.resetPassword(prefHelper.getSignUpUser().getId(), edtPassword.getText().toString() + "", edtCfmPassword.getText().toString() + ""), WebServiceConstants.RESEND_PASSWORD);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.RESEND_PASSWORD:
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.pass_reset));
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                break;
        }
    }
}
