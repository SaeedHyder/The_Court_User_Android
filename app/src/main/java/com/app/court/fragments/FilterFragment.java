package com.app.court.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.app.court.R;
import com.app.court.entities.FilterEnt;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.binders.BinderFilter;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.CustomRecyclerView;
import com.app.court.ui.views.ExpandedListView;
import com.app.court.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FilterFragment extends BaseFragment {

    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_filter)
    CustomRecyclerView lvFilter;
    Unbinder unbinder;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private ArrayListAdapter<FilterEnt> adapter;
    private ArrayList<String> selectedItems=new ArrayList<>();

    public static FilterFragment newInstance() {
        Bundle args = new Bundle();

        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  adapter = new ArrayListAdapter<>(getDockActivity(), new BinderFilter(getDockActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindData();
    }

    private void bindData() {

        lvFilter.BindRecyclerView(new BinderFilter(getDockActivity()), getMainActivity().getCollection(),
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());
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
                selectedItems.clear();
                for(FilterEnt item: getMainActivity().getCollection()){
                    item.setChecked(false);
                }
                lvFilter.getAdapter().notifyDataSetChanged();
            }
        });
    }



    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if(getMainActivity().getCollection().size()>0){
            for(FilterEnt item: getMainActivity().getCollection()){
                if(item.isChecked()){
                selectedItems.add(item.getId()+"");
                }
            }
        }
        String selectedFilters=android.text.TextUtils.join(",", selectedItems);

        getDockActivity().replaceDockableFragment(MyCaseFragment.newInstance(selectedFilters), "MyCaseFragment");
    }
}
