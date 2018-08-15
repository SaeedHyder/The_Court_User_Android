package com.app.court.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.court.R;
import com.app.court.entities.FindLawyerEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


public class LawyerProfileFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_heading)
    AnyTextView tvHeading;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.tv_lawyer_name)
    AnyTextView tvLawyerName;
    @BindView(R.id.tv_specialization)
    AnyTextView tvSpecialization;
    @BindView(R.id.tv_experience)
    AnyTextView tvExperience;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.tv_acedamic)
    AnyTextView tvAcedamic;
    @BindView(R.id.tv_affiliation)
    AnyTextView tvAffiliation;
    @BindView(R.id.tv_languages)
    AnyTextView tvLanguages;
    @BindView(R.id.v2)
    View v2;
    @BindView(R.id.btn_submit_case)
    Button btnSubmitCase;
    @BindView(R.id.civ_lawyer)
    CircleImageView civLawyer;
    private static String lawyer_key = "lawyer_key";
    private FindLawyerEntity entity;
    ImageLoader imageLoader;

    public static LawyerProfileFragment newInstance(FindLawyerEntity entity) {
        Bundle args = new Bundle();
        args.putString(lawyer_key, new Gson().toJson(entity));
        LawyerProfileFragment fragment = new LawyerProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        if (getArguments() != null) {
            lawyer_key = getArguments().getString(lawyer_key);
        }
        if (lawyer_key != null) {
            entity = new Gson().fromJson(lawyer_key, FindLawyerEntity.class);
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.hideTitleBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_lawyer, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageLoader.displayImage(entity.getImageUrl(), civLawyer);
        tvLawyerName.setText(entity.getFullName() + "");

        int i;
        StringBuilder commaSeparatedNames = new StringBuilder();
        for (i = 0; i < entity.getSpecializations().size(); i++) {
            if (i == 0) {
                commaSeparatedNames.append(entity.getSpecializations().get(i).getSpecializationDetail().getTitle());
            } else {
                commaSeparatedNames.append(", " + entity.getSpecializations().get(i).getSpecializationDetail().getTitle());
            }
        }
        String specialization = "<b><font color='#000'>Specialization: </font></b>";
        tvSpecialization.setText(Html.fromHtml(specialization + " " + commaSeparatedNames), TextView.BufferType.SPANNABLE);

        String experience = "<b><font color='#000'>Experience: </font></b>";
        tvExperience.setText(Html.fromHtml(experience + " " + entity.getExperienceDetail().getTitle()), TextView.BufferType.SPANNABLE);

        String acedamic = "<font color='#000'>Academic: </font>";
        tvAcedamic.setText(Html.fromHtml(acedamic + " " + entity.getAcademic()), TextView.BufferType.SPANNABLE);

        String affiliation = "<font color='#000'>Affiliation: </font>";
        tvAffiliation.setText(Html.fromHtml(affiliation + " " + entity.getAffilationDetail().getTitle()), TextView.BufferType.SPANNABLE);

        int b;
        StringBuilder commaSeparatedLanguage = new StringBuilder();
        for (b = 0; b < entity.getLanguages().size(); b++) {
            if (b == 0) {
                commaSeparatedNames.append(entity.getLanguages().get(b).getLanguageDetail().getTitle());
            } else {
                commaSeparatedNames.append(", " + entity.getLanguages().get(b).getLanguageDetail().getTitle());
            }
        }
        String Languages = "<font color='#000'>Languages: </font><font color='#858992'>English, Arabic</font>";
        tvLanguages.setText(Html.fromHtml(Languages + " " + commaSeparatedLanguage), TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_back, R.id.btn_submit_case})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getMainActivity().popFragment();
                break;
            case R.id.btn_submit_case:
                getDockActivity().replaceDockableFragment(SubmitCaseFragment.newInstance(entity), "SubmitCaseFragment");
                break;
        }
    }
}
