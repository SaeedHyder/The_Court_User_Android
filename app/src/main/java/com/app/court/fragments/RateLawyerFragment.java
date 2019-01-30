package com.app.court.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.court.R;
import com.app.court.entities.MyCaseEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.CustomRatingBar;
import com.app.court.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RateLawyerFragment extends BaseFragment {
    @BindView(R.id.rating_bar)
    CustomRatingBar ratingBar;
    @BindView(R.id.btn_signup)
    Button btnSignup;
    Unbinder unbinder;
    static String actionId;
    @BindView(R.id.txt_text)
    AnyTextView txtText;
    MyCaseEntity response;
    @BindView(R.id.mainFrame)
    LinearLayout mainFrame;

    public static RateLawyerFragment newInstance(String id) {
        Bundle args = new Bundle();
        actionId = id;
        RateLawyerFragment fragment = new RateLawyerFragment();
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
        View view = inflater.inflate(R.layout.fragment_rate_lawyer, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainFrame.setVisibility(View.GONE);
        if (actionId != null) {
            serviceHelper.enqueueCall(webService.getCaseDetail(actionId + ""), WebServiceConstants.CaseDetail);
        }

        ratingBar.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                if (score < 1.0f)
                    ratingBar.setScore(1.0f);
            }
        });

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);
        switch (Tag) {
            case WebServiceConstants.CaseDetail:
                mainFrame.setVisibility(View.VISIBLE);
                response = (MyCaseEntity) result;
                txtText.setText(getString(R.string.kindly_rate) + " " + response.getLawyerDetail().getFullName() + " " + getString(R.string.scale));
                break;

            case WebServiceConstants.RateLawyer:
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.rating_submitted_successfully));
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.rate_and_review));
    }


    @OnClick(R.id.btn_signup)
    public void onViewClicked() {

        serviceHelper.enqueueCall(webService.lawyerRate(response.getLawyerId() + "", actionId + "", Math.round(ratingBar.getScore())), WebServiceConstants.RateLawyer);
    }



}
