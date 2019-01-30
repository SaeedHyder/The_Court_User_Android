package com.app.court.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.court.R;
import com.app.court.entities.EntityCms;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.global.WebServiceConstants;
import com.app.court.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TermsAndConditionFragment extends BaseFragment {
    @BindView(R.id.txt_term_condition)
    TextView txtTermCondition;
    @BindView(R.id.btn_i_agree)
    Button btnIAgree;
    Unbinder unbinder;
    private static String comingFrom = "";

    public static TermsAndConditionFragment newInstance(String comingFromWhere) {
        Bundle args = new Bundle();
        comingFrom = comingFromWhere;
        TermsAndConditionFragment fragment = new TermsAndConditionFragment();
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
        View view = inflater.inflate(R.layout.fragment_termns_condition, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getTermsAndCondition();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().popFragment();
              /*  if (comingFrom.equalsIgnoreCase(AppConstants.COMING_FROM_SIGNUP)){
                    getDockActivity().replaceDockableFragment(SignupFragment.newInstance(), "LoginFragment");
                } else {
                    getMainActivity().popFragment();
                }*/
            }
        });
        titleBar.setSubHeading(getString(R.string.terms_condition));
    }

    @OnClick(R.id.btn_i_agree)
    public void onViewClicked() {
        if (comingFrom.equalsIgnoreCase(AppConstants.COMING_FROM_SIGNUP)) {
            getDockActivity().popBackStackTillEntry(1);
            getDockActivity().replaceDockableFragment(EnterCodeFragment.newInstance(AppConstants.COMING_FROM_SIGNUP), "EnterCodeFragment");
        } else {
            getMainActivity().popFragment();
        }
    }

    private void getTermsAndCondition() {
        serviceHelper.enqueueCall(webService.getCms(AppConstants.CMS_TERMS), WebServiceConstants.GET_CMS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_CMS:
                EntityCms entity = (EntityCms) result;
                txtTermCondition.setText(entity.getDescription() + "");
                break;
        }
    }
}
