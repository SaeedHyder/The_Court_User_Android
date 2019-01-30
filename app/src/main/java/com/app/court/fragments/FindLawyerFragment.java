package com.app.court.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.court.R;
import com.app.court.entities.FindLawyerEntity;
import com.app.court.entities.MyCaseEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.binders.BinderFindLawyer;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FindLawyerFragment extends BaseFragment {

    @BindView(R.id.edt_name)
    AnyEditTextView edtName;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.lv_lawyers)
    ListView lvLawyers;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    private ArrayList<String> collection;
    private ArrayListAdapter<FindLawyerEntity> adapter;
    private static String LATITUDE = "";
    private static String LONGITUDE = "";
    private static String RATE = "";
    private static String SPECIALIZATION_IDS = "";
    private static String LANGUAGE_IDS = "";
    private static String AFFILIATION_IDS = "";
    private String NAME_SEARCH = "";
    private ArrayList<FindLawyerEntity> getLawyers;
    private ArrayList<FindLawyerEntity> userCollection;
    private static boolean isFilter = false;
    Unbinder unbinder;

    public static FindLawyerFragment newInstance(String affiliationId, String specializationId, String languageId, String RATING, String LOCATION, String LAT, String LONG) {
        Bundle args = new Bundle();
        LATITUDE = LAT;
        LONGITUDE = LONG;
        AFFILIATION_IDS = affiliationId;
        LANGUAGE_IDS = languageId;
        SPECIALIZATION_IDS = specializationId;
        RATE = RATING;
        isFilter = true;
        FindLawyerFragment fragment = new FindLawyerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static FindLawyerFragment newInstance() {
        Bundle args = new Bundle();
        isFilter = false;
        FindLawyerFragment fragment = new FindLawyerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderFindLawyer(getDockActivity()));
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_lawyer, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchingName();

        if (isFilter) {
            getSpecialization();
        } else
            getSpecializationWithoutFilters();
    }

    private void searchingName() {
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bindData(getSearchedArray(s.toString()));
            }
        });
    }

    public ArrayList<FindLawyerEntity> getSearchedArray(String keyword) {
        if (userCollection != null)
            if (userCollection.isEmpty()) {
                return new ArrayList<>();
            }

        ArrayList<FindLawyerEntity> arrayList = new ArrayList<>();

        String UserName = "";
        if (userCollection != null)
            for (FindLawyerEntity item : userCollection) {
                UserName = item.getFullName();
                if (Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE).matcher(UserName).find()) {
                    arrayList.add(item);
                }
            }
        return arrayList;

    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.lawyers));
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
            }
        });
        titleBar.showFilterButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().popBackStackTillEntry(1);
                getDockActivity().replaceDockableFragment(FilterLawyer.newInstance(), "FilterLawyer");
            }
        });
    }


    @OnClick(R.id.iv_search)
    public void onViewClicked() {
        UIHelper.hideSoftKeyboard(getDockActivity(), getDockActivity().getWindow().getDecorView());
        getSpecialization();
    }

    private void getSpecializationWithoutFilters() {

        LATITUDE = "";
        LONGITUDE = "";
        RATE = "";
        SPECIALIZATION_IDS = "";
        LANGUAGE_IDS = "";
        AFFILIATION_IDS = "";

        serviceHelper.enqueueCall(webService.getLawyers(LATITUDE, LONGITUDE, RATE, SPECIALIZATION_IDS, LANGUAGE_IDS, AFFILIATION_IDS, edtName.getText().toString()), WebServiceConstants.FIND_LAWYERS);
    }

    private void getSpecialization() {
        if (LATITUDE == null)
            LATITUDE = "";

        if (LONGITUDE == null)
            LONGITUDE = "";

        if (RATE == null || RATE.equals("0"))
            RATE = "";

        if (SPECIALIZATION_IDS == null)
            SPECIALIZATION_IDS = "";

        if (LANGUAGE_IDS == null)
            LANGUAGE_IDS = "";

        if (AFFILIATION_IDS == null)
            AFFILIATION_IDS = "";

        serviceHelper.enqueueCall(webService.getLawyers(LATITUDE, LONGITUDE, RATE, SPECIALIZATION_IDS, LANGUAGE_IDS, AFFILIATION_IDS, edtName.getText().toString()), WebServiceConstants.FIND_LAWYERS);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.FIND_LAWYERS:
                getLawyers = (ArrayList<FindLawyerEntity>) result;
                geMyLawyersListData(getLawyers);
                break;
        }
    }

    private void geMyLawyersListData(ArrayList<FindLawyerEntity> result) {
        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<FindLawyerEntity> lawyerCollection) {

        if (lawyerCollection.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            lvLawyers.setVisibility(View.VISIBLE);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            lvLawyers.setVisibility(View.GONE);
        }

        adapter.clearList();
        lvLawyers.setAdapter(adapter);
        adapter.addAll(lawyerCollection);
    }
}
