package com.app.court.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.court.R;
import com.app.court.entities.MainPaymentEntity;
import com.app.court.entities.MyCaseEntity;
import com.app.court.entities.PaymentEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class PaymentDetailFragment extends BaseFragment {

    @BindView(R.id.tv_case_type)
    AnyTextView tvCaseType;
    @BindView(R.id.tv_date)
    AnyTextView tvDate;
    @BindView(R.id.tv_lawyer_name)
    AnyTextView tvLawyerName;
    @BindView(R.id.tv_case_name)
    AnyTextView tvCaseName;
    @BindView(R.id.tv_amount)
    AnyTextView tvAmount;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private static String CASE_KEY_PAYMENT = "CASE_KEY_PAYMENT";
    private MyCaseEntity entity;
    private static String ID;
    Unbinder unbinder;

    public static PaymentDetailFragment newInstance() {
        Bundle args = new Bundle();
        PaymentDetailFragment fragment = new PaymentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentDetailFragment newInstance(MyCaseEntity entity, String paymentId) {
        Bundle args = new Bundle();
        args.putString(CASE_KEY_PAYMENT, new Gson().toJson(entity));
        ID = paymentId;
        PaymentDetailFragment fragment = new PaymentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CASE_KEY_PAYMENT = getArguments().getString(CASE_KEY_PAYMENT);
        }
        if (CASE_KEY_PAYMENT != null) {
            entity = new Gson().fromJson(CASE_KEY_PAYMENT, MyCaseEntity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.make_payment));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }



    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        payNow();
    }

    private void payNow() {
        serviceHelper.enqueueCall(webService.payDuePayment(Integer.parseInt(ID)), WebServiceConstants.PAY_NOW);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.PAY_NOW:
                getDockActivity().popFragment();
                UIHelper.showShortToastInCenter(getDockActivity(), "Payment successful.");
                break;
        }
    }
}
