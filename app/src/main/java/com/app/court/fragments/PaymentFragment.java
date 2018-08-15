package com.app.court.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.court.R;
import com.app.court.entities.MyCaseEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.court.activities.DockActivity.KEY_FRAG_FIRST;


public class PaymentFragment extends BaseFragment {

    @BindView(R.id.ll_due)
    LinearLayout llDue;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;
    @BindView(R.id.mainFrame)
    LinearLayout mainFrame;
    @BindView(R.id.tv_due_payment)
    AnyTextView tvDuePayment;
    @BindView(R.id.view_due)
    View viewDue;
    @BindView(R.id.tv_history_payment)
    AnyTextView tvHistoryPayment;
    @BindView(R.id.view_history)
    View viewHistory;

    private DuePaymentFragment duePaymentFragment;
    private HistoryPaymentFragment historyPaymentFragment;
    private static int ID;

    private static String CASE_KEY_PAYMENT = "CASE_KEY_PAYMENT";
    private MyCaseEntity entity;
    Unbinder unbinder;

    public static PaymentFragment newInstance() {
        Bundle args = new Bundle();
        PaymentFragment fragment = new PaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentFragment newInstance(MyCaseEntity entity) {
        Bundle args = new Bundle();
        args.putString(CASE_KEY_PAYMENT, new Gson().toJson(entity));
        PaymentFragment fragment = new PaymentFragment();
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
        duePaymentFragment = DuePaymentFragment.newInstance(entity);
        historyPaymentFragment = HistoryPaymentFragment.newInstance(entity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ReplaceListViewFragment(duePaymentFragment);
    }

    private void ReplaceListViewFragment(BaseFragment frag) {

        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.mainFrame, frag);
        transaction
                .addToBackStack(
                        getChildFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading("My Case");
        titleBar.showBackButton();
        titleBar.showFilterButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(PaymentFilterFragment.newInstance(),"PaymentFilterFragment");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.ll_due, R.id.ll_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_due:
                tvDuePayment.setTextColor(getResources().getColor(R.color.app_orange));
                tvHistoryPayment.setTextColor(getResources().getColor(R.color.light_grey));
                viewDue.setVisibility(View.VISIBLE);
                viewHistory.setVisibility(View.INVISIBLE);
                ReplaceListViewFragment(duePaymentFragment);
                break;
            case R.id.ll_history:
                tvDuePayment.setTextColor(getResources().getColor(R.color.light_grey));
                tvHistoryPayment.setTextColor(getResources().getColor(R.color.app_orange));
                viewHistory.setVisibility(View.VISIBLE);
                viewDue.setVisibility(View.INVISIBLE);
                ReplaceListViewFragment(historyPaymentFragment);
                break;
        }
    }
}
