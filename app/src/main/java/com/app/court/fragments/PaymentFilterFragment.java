package com.app.court.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.court.R;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class PaymentFilterFragment extends BaseFragment {

    @BindView(R.id.cb_latest_to_old)
    RadioButton cbLatestToOld;
    @BindView(R.id.cb_old_to_latest)
    RadioButton cbOldToLatest;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    public static PaymentFilterFragment newInstance() {

        Bundle args = new Bundle();

        PaymentFilterFragment fragment = new PaymentFilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cbLatestToOld.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefHelper.setPaymentFilter(AppConstants.LATEST_TO_OLD);
            }
        });
        cbOldToLatest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefHelper.setPaymentFilter(AppConstants.OLD_TO_LATEST);
            }
        });

       /* cbLatestToOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((RadioButton) v).isChecked()) {
                    cbLatestToOld.setChecked(true);
                } else {
                    cbLatestToOld.setChecked(false);
                }
                prefHelper.setPaymentFilter(AppConstants.LATEST_TO_OLD);
            }
        });

        cbOldToLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((RadioButton) v).isChecked()) {
                    cbOldToLatest.setChecked(true);
                } else {
                    cbOldToLatest.setChecked(false);
                }
                prefHelper.setPaymentFilter(AppConstants.OLD_TO_LATEST);
            }
        });

        */

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.filter));
        titleBar.showBackButton();
        titleBar.showResetButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroup.clearCheck();
                prefHelper.setPaymentFilter(AppConstants.LATEST_TO_OLD);
            }
        });
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        getMainActivity().popFragment();
    }


}
