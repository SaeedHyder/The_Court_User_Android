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
import com.app.court.helpers.DateHelper;
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
    private static String PAYMENT_ENTITY = "PAYMENT_ENTITY";
    private MyCaseEntity entity;
    private PaymentEntity payment_Entity;
    private static String ID;
    Unbinder unbinder;

    public static PaymentDetailFragment newInstance() {
        Bundle args = new Bundle();
        PaymentDetailFragment fragment = new PaymentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentDetailFragment newInstance(MyCaseEntity entity, String paymentId, PaymentEntity paymentEntity) {
        Bundle args = new Bundle();
        args.putString(CASE_KEY_PAYMENT, new Gson().toJson(entity));
        args.putString(PAYMENT_ENTITY, new Gson().toJson(paymentEntity));
        ID = paymentId;
        PaymentDetailFragment fragment = new PaymentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String PaymentJsonString;
        String CaseKeyJsonString;

        if (getArguments() != null) {
            CaseKeyJsonString = getArguments().getString(CASE_KEY_PAYMENT);
            PaymentJsonString = getArguments().getString(PAYMENT_ENTITY);

            if (PaymentJsonString != null) {
                payment_Entity = new Gson().fromJson(PaymentJsonString, PaymentEntity.class);
            }

            if (CaseKeyJsonString != null) {
                entity = new Gson().fromJson(CaseKeyJsonString, MyCaseEntity.class);
            }
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

        tvCaseType.setText(payment_Entity.getTitle() + "");
        tvCaseName.setText(entity.getDetail() + "");
        tvLawyerName.setText(entity.getLawyerDetail().getFullName() + "");
        tvDate.setText(DateHelper.getLocalDatePayment(entity.getCreatedAt() + ""));
        tvAmount.setText("$" + payment_Entity.getCharges()+ "");

      /*  if (entity.getPayments().size() > 0)
            tvAmount.setText("$" + entity.getPayments().get(0).getCharges() + "");*/

    }


    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        payNow();
    }

    private void payNow() {
        serviceHelper.enqueueCall(webService.payDuePayment(Integer.parseInt(ID)), WebServiceConstants.PAY_NOW);
        //serviceHelper.enqueueCall(webService.payDuePayment(entity.getId()), WebServiceConstants.PAY_NOW);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.PAY_NOW:

                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.payment_successfully));
                break;
        }
    }
}
