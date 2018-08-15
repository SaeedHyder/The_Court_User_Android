package com.app.court.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.court.R;
import com.app.court.entities.MainPaymentEntity;
import com.app.court.entities.MyCaseEntity;
import com.app.court.entities.PaymentEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.binders.BinderPayments;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.AnyTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HistoryPaymentFragment extends BaseFragment {

    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_payment)
    ListView lvPayment;
    MainPaymentEntity mainPaymentEntity;
    @BindView(R.id.edt_name)
    AnyEditTextView edtName;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.mainFrame)
    LinearLayout mainFrame;
    private ArrayListAdapter<PaymentEntity> adapter;
    private ArrayList<PaymentEntity> paymentEntity;
    private ArrayList<PaymentEntity> userCollection=new ArrayList<>();
    private static String CASE_KEY_PAYMENT = "CASE_KEY_PAYMENT";
    private MyCaseEntity entity;
    Unbinder unbinder;

    public static HistoryPaymentFragment newInstance(MyCaseEntity entity) {
        Bundle args = new Bundle();
        args.putString(CASE_KEY_PAYMENT, new Gson().toJson(entity));
        HistoryPaymentFragment fragment = new HistoryPaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static HistoryPaymentFragment newInstance() {
        Bundle args = new Bundle();

        HistoryPaymentFragment fragment = new HistoryPaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderPayments(getDockActivity()));
        if (getArguments() != null) {
            CASE_KEY_PAYMENT = getArguments().getString(CASE_KEY_PAYMENT);
        }
        if (CASE_KEY_PAYMENT != null) {
            entity = new Gson().fromJson(CASE_KEY_PAYMENT, MyCaseEntity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_due_payment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainFrame.setVisibility(View.GONE);
        getMyCase();
        searchingName();
    }

    private void getMyCase() {
        serviceHelper.enqueueCall(webService.casePayment(entity.getId(), prefHelper.getPaymentFilter()), WebServiceConstants.PAYMENT_HISTORY);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.PAYMENT_HISTORY:
                mainFrame.setVisibility(View.VISIBLE);
                mainPaymentEntity = (MainPaymentEntity) result;
                txtNoData.setText(R.string.no_payment_history_found);
                paymentEntity = mainPaymentEntity.getPaidPayment();
                if (paymentEntity.size() <= 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                    lvPayment.setVisibility(View.GONE);
                } else {
                    txtNoData.setVisibility(View.GONE);
                    lvPayment.setVisibility(View.VISIBLE);
                    geMyCasesListData(paymentEntity);
                }
                break;
        }
    }

    private void geMyCasesListData(ArrayList<PaymentEntity> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<PaymentEntity> paymentCollection) {

        adapter.clearList();
        lvPayment.setAdapter(adapter);
        adapter.addAll(paymentCollection);
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


    public ArrayList<PaymentEntity> getSearchedArray(String keyword) {
        if (userCollection != null)
            if (userCollection.isEmpty()) {
                return new ArrayList<>();
            }

        ArrayList<PaymentEntity> arrayList = new ArrayList<>();

        String UserName = "";
        for (PaymentEntity item : userCollection) {
            UserName = item.getTitle();
            if (Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE).matcher(UserName).find()) {
                arrayList.add(item);
            }
        }
        return arrayList;

    }

/*    private void bindData(ArrayList<Post> collection) {

        Adapter.clearList();
        lvPromotions.setAdapter(Adapter);
        Adapter.addAll(collection);
    }*/

/*    public ArrayList<PaymentEntity> getSearchedArray(String keyword) {
        if (userCollection != null)
            if (userCollection.isEmpty()) {
                return new ArrayList<>();
            }

        ArrayList<PaymentEntity> arrayList = new ArrayList<>();
        if (userCollection != null)
            for (PaymentEntity item : userCollection) {
                String UserName = "";
                if (item != null) {
                    UserName = item.getTitle();
                }
                if (Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE).matcher(UserName).find()) {
                    arrayList.add(item);
                }
            }
        return arrayList;
    }*/


}
