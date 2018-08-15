package com.app.court.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class ChangePasswordFragment extends BaseFragment {
    @BindView(R.id.txt_current_password)
    AnyEditTextView txtCurrentPassword;
    @BindView(R.id.txt_New_Password)
    AnyEditTextView txtNewPassword;
    @BindView(R.id.txt_Confirm_Password)
    AnyEditTextView txtConfirmPassword;
    @BindView(R.id.btn_update_password)
    Button btnUpdatePassword;
    Unbinder unbinder;

    public static ChangePasswordFragment newInstance() {
        Bundle args = new Bundle();

        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private boolean isvalidate() {
        if (txtCurrentPassword.getText() == null || (txtCurrentPassword.getText().toString().isEmpty())) {
            txtCurrentPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (txtNewPassword.getText() == null || (txtNewPassword.getText().toString().isEmpty())) {
            txtNewPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (txtNewPassword.getText().toString().equals(txtCurrentPassword.getText().toString())) {
            txtNewPassword.setError(getString(R.string.samePassword));
            return false;
        } else if (txtNewPassword.getText().toString().length() < 6) {
            txtNewPassword.setError(getString(R.string.passwordLength));
            return false;
        } else if (txtConfirmPassword.getText() == null || (txtConfirmPassword.getText().toString().isEmpty())) {
            txtConfirmPassword.setError(getString(R.string.enter_confirm_password));
            return false;
        } else if (txtConfirmPassword.getText().toString().length() < 6) {
            txtConfirmPassword.setError(getString(R.string.confirmpasswordLength));
            return false;
        } else if (!txtConfirmPassword.getText().toString().equals(txtNewPassword.getText().toString())) {
            txtConfirmPassword.setError(getString(R.string.conform_password_error));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.change_password));
    }


    @OnClick(R.id.btn_update_password)
    public void onViewClicked() {
        if (isvalidate()) {
            changePassword();
        }
    }

    private void changePassword() {
        serviceHelper.enqueueCall(webService.changePassword(txtCurrentPassword.getText().toString() + "", txtNewPassword.getText().toString() + "", txtConfirmPassword.getText().toString() + ""), WebServiceConstants.CHANGE_PASSWORD);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.CHANGE_PASSWORD:
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.pass_changed));
                getDockActivity().replaceDockableFragment(SettingsFragment.newInstance(), "SettingsFragment");
                break;
        }
    }
}
