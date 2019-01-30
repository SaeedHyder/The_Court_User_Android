package com.app.court.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.court.R;
import com.app.court.entities.MessageThreadEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.binders.BinderMessagesThread;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MessagesThreadFragment extends BaseFragment {


    @BindView(R.id.lv_messages_thread)
    ListView lvMessagesThread;
    @BindView(R.id.edt_name)
    AnyEditTextView edtName;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayListAdapter<MessageThreadEntity> adapter;
    private ArrayList<MessageThreadEntity> getThread;
    private ArrayList<MessageThreadEntity> userCollection;

    Unbinder unbinder;

    public static MessagesThreadFragment newInstance() {
        return new MessagesThreadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderMessagesThread(getDockActivity(), prefHelper));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages_thread, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getThread();
        searchingName();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.messages));
    }

    private void getThread() {
        serviceHelper.enqueueCall(webService.getMessageThread(), WebServiceConstants.MESSAGE_THREAD);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.MESSAGE_THREAD:
                getThread = (ArrayList<MessageThreadEntity>) result;
                geMyCasesListData(getThread);
                break;
        }
    }

    private void geMyCasesListData(ArrayList<MessageThreadEntity> result) {
        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<MessageThreadEntity> threadCollection) {

        if (threadCollection.size() > 0) {
            lvMessagesThread.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
        } else {
            lvMessagesThread.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        }

        adapter.clearList();
        lvMessagesThread.setAdapter(adapter);
        adapter.addAll(threadCollection);

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


    public ArrayList<MessageThreadEntity> getSearchedArray(String keyword) {
        if (userCollection != null)
            if (userCollection.isEmpty()) {
                return new ArrayList<>();
            }

        ArrayList<MessageThreadEntity> arrayList = new ArrayList<>();

        String UserName = "";
        if (userCollection != null)
            for (MessageThreadEntity item : userCollection) {
                if (item.getCaseDetail() != null) {
                    UserName = item.getCaseDetail().getSubject();
                    if (Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE).matcher(UserName).find()) {
                        arrayList.add(item);
                    }
                }
            }
        return arrayList;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.iv_search)
    public void onViewClicked() {
        UIHelper.HideKeyBoard(getDockActivity());
    }
}
