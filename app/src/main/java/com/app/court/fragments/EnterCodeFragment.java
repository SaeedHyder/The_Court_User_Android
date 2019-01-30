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
import com.app.court.helpers.TokenUpdater;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.PinEntryEditText;
import com.app.court.ui.views.TitleBar;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EnterCodeFragment extends BaseFragment {


    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText txtPinEntry;
    @BindView(R.id.tv_resend_code)
    AnyTextView tvResendCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    Unbinder unbinder;
    private static String comingFrom = "";

    public static EnterCodeFragment newInstance() {
        return new EnterCodeFragment();
    }

    public static EnterCodeFragment newInstance(String comingFromSignupWhere) {
        comingFrom = comingFromSignupWhere;
        return new EnterCodeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_code, container, false);
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

    @OnClick({R.id.back_btn, R.id.tv_resend_code, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                getMainActivity().popFragment();
                break;
            case R.id.tv_resend_code:
                resendCode();
                break;
            case R.id.btn_login:
                if ((txtPinEntry.getText().toString().length() < 4)) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.enter_code));
                } else {
                    enterSignupCode();
                }
                break;
        }
    }

    private void enterSignupCode() {
        if (prefHelper != null && prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getId() != null)
            serviceHelper.enqueueCall(webService.signupCodeVerification(prefHelper.getSignUpUser().getId(), txtPinEntry.getText().toString() + ""), WebServiceConstants.SIGNUP_CODE_VERIFICATION);
    }

    private void resendCode() {
        if (prefHelper != null && prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getId() != null)
            serviceHelper.enqueueCall(webService.resendCode(prefHelper.getSignUpUser().getId()), WebServiceConstants.RESEND_CODE);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.SIGNUP_CODE_VERIFICATION:
                SignUpEntity entity = (SignUpEntity) result;
                prefHelper.putSignupUser(entity);
                TokenUpdater.getInstance().UpdateToken(getDockActivity(),
                        entity.getId() + "",
                        AppConstants.Device_Type,
                        FirebaseInstanceId.getInstance().getToken());
                if (comingFrom.equalsIgnoreCase(AppConstants.COMING_FROM_SIGNUP)) {
                    getDockActivity().popBackStackTillEntry(0);
                    prefHelper.setLoginStatus(true);
                    getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                } else {
                    getDockActivity().popBackStackTillEntry(1);
                    getDockActivity().replaceDockableFragment(ResetPasswordFragment.newInstance(), "ResetPasswordFragment");
                }
                break;

            case WebServiceConstants.RESEND_CODE:
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.new_code));
                break;
        }
    }
}
