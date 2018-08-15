package com.app.court.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.court.R;
import com.app.court.entities.MyCaseEntity;
import com.app.court.entities.PaymentEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.DialogHelper;
import com.app.court.helpers.UIHelper;
import com.app.court.interfaces.CaseItemClick;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.binders.MyCaseBinder;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyCaseFragment extends BaseFragment implements CaseItemClick {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_my_case)
    ListView lvMyCase;
    Unbinder unbinder;
    @BindView(R.id.edt_name)
    AnyEditTextView edtName;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    private ArrayListAdapter<MyCaseEntity> adapter;
    private ArrayList<MyCaseEntity> getCases;
    private ArrayList<MyCaseEntity> userCollection;

    private static String SELECTED_FILTER_ARRAY = "";

    public static MyCaseFragment newInstance(String filterSelected) {
        Bundle args = new Bundle();
        SELECTED_FILTER_ARRAY = filterSelected;
        MyCaseFragment fragment = new MyCaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MyCaseFragment newInstance() {
        Bundle args = new Bundle();

        MyCaseFragment fragment = new MyCaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new MyCaseBinder(getDockActivity(), prefHelper, this));
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_case, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMyCase();

        searchingName();
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
            }
        });
        titleBar.setSubHeading(getString(R.string.my_case));
        titleBar.showFilterButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(FilterFragment.newInstance(), "FilterFragment");
            }
        });
    }

    private void getMyCase() {
        if (SELECTED_FILTER_ARRAY != null && !(SELECTED_FILTER_ARRAY.equals(""))) {
            serviceHelper.enqueueCall(webService.getMyCaseFilter(SELECTED_FILTER_ARRAY), WebServiceConstants.MY_CASE);
        } else {
            serviceHelper.enqueueCall(webService.getMyCase(), WebServiceConstants.MY_CASE);
        }
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


    public ArrayList<MyCaseEntity> getSearchedArray(String keyword) {
        if (userCollection != null)
            if (userCollection.isEmpty()) {
                return new ArrayList<>();
            }

        ArrayList<MyCaseEntity> arrayList = new ArrayList<>();

        String UserName = "";
        for (MyCaseEntity item : userCollection) {
            UserName = item.getSubject();
            if (Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE).matcher(UserName).find()) {
                arrayList.add(item);
            }
        }
        return arrayList;

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.MY_CASE:

                geMyCasesListData((ArrayList<MyCaseEntity>) result);
                break;
        }
    }

    private void geMyCasesListData(ArrayList<MyCaseEntity> result) {
        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<MyCaseEntity> lawyerCollection) {

        if (lawyerCollection.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            lvMyCase.setVisibility(View.VISIBLE);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            lvMyCase.setVisibility(View.GONE);

        }

        adapter.clearList();
        lvMyCase.setAdapter(adapter);
        adapter.addAll(lawyerCollection);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.iv_search)
    public void onViewClicked() {
        UIHelper.HideKeyBoard(getDockActivity());
        searchingName();
    }

    @Override
    public void onClick(MyCaseEntity entity, int position) {
        //if (!entity.getStatus().equals("7")) {
        getDockActivity().replaceDockableFragment(CaseDescriptionFragment.newInstance(entity), "CaseDescriptionFragment");
        // }
    }
}
