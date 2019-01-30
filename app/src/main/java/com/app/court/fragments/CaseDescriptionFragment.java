package com.app.court.fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.court.R;
import com.app.court.entities.CommentResponseEnt;
import com.app.court.entities.CommentsEntity;
import com.app.court.entities.MyCaseEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.DateHelper;
import com.app.court.helpers.DialogHelper;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.binders.MyCaseDescriptionBinder;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.ExpandedListView;
import com.app.court.ui.views.TitleBar;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.court.global.WebServiceConstants.WITHDRAWCASE;

public class CaseDescriptionFragment extends BaseFragment {
    @BindView(R.id.ll_description)
    LinearLayout llDescription;
    @BindView(R.id.iv_messages)
    ImageView ivMessages;
    @BindView(R.id.iv_library)
    ImageView ivLibrary;
    @BindView(R.id.tv_lawyer_name)
    AnyTextView tvLawyerName;
    @BindView(R.id.tv_date)
    AnyTextView tvDate;
    @BindView(R.id.tv_subject)
    AnyTextView tvSubject;
    @BindView(R.id.tv_case_description)
    AnyTextView tvCaseDescription;
    @BindView(R.id.btn_payment)
    Button btnPayment;
    @BindView(R.id.btn_mark_as_complete)
    Button btnMarkAsComplete;
    @BindView(R.id.ll_add_comments)
    LinearLayout llAddComments;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_comments)
    ExpandedListView lvComments;
    DialogHelper dialogHelper;
    private MyCaseEntity getCases;
    private ArrayList<CommentsEntity> commentsEntity;
    private ArrayList<CommentsEntity> userCollection;

    private ArrayList<CommentsEntity> collection;
    private ArrayListAdapter<CommentsEntity> adapter;

    private static String CASE_KEY = "CASE_KEY";
    private MyCaseEntity entity;
    Unbinder unbinder;

    public static CaseDescriptionFragment newInstance(MyCaseEntity entity) {
        Bundle args = new Bundle();
        args.putString(CASE_KEY, new Gson().toJson(entity));
        CaseDescriptionFragment fragment = new CaseDescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<>(getDockActivity(), new MyCaseDescriptionBinder(getDockActivity(), prefHelper));
        if (getArguments() != null) {
            CASE_KEY = getArguments().getString(CASE_KEY);
        }
        if (CASE_KEY != null) {
            entity = new Gson().fromJson(CASE_KEY, MyCaseEntity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_description, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (entity.getStatus().equals("1") && entity.getAllowMessage() == 0) {
            ivMessages.setImageResource(R.drawable.disable);
            btnMarkAsComplete.setText(getDockActivity().getResources().getString(R.string.withdraw));
            btnMarkAsComplete.setVisibility(View.VISIBLE);
        } else if (entity.getStatus().equals("2") && entity.getAllowMessage() == 0) {
            ivMessages.setImageResource(R.drawable.disable);
            btnMarkAsComplete.setVisibility(View.GONE);
        } else if (entity.getStatus().equals("2") && entity.getAllowMessage() == 1) {
            ivMessages.setImageResource(R.drawable.messages);
            btnMarkAsComplete.setText(getDockActivity().getResources().getString(R.string.mark_as_complete));
            btnMarkAsComplete.setVisibility(View.VISIBLE);
        }

        if (entity.getStatus().equals("3") || entity.getStatus().equals("4")) {
            ivMessages.setImageResource(R.drawable.disable);
            btnPayment.setVisibility(View.GONE);
            btnMarkAsComplete.setVisibility(View.GONE);
        } else if (entity.getStatus().equals("5") || entity.getStatus().equals("7")) {
            btnPayment.setVisibility(View.GONE);
            btnMarkAsComplete.setVisibility(View.GONE);
            ivMessages.setImageResource(R.drawable.messages);
        }

        tvLawyerName.setText(entity.getLawyerDetail().getFullName() + "");
        tvSubject.setText(entity.getSubject() + "");
        tvCaseDescription.setText(entity.getDetail() + "");
        tvDate.setText((DateHelper.getLocalDate(entity.getCreatedAt()) + " | " + (DateHelper.getLocalTime(entity.getCreatedAt()))));
        bindData(entity.getComments());

    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(MyCaseFragment.newInstance(), "MyCaseFragment");
            }
        });
        titleBar.setSubHeading(entity.getSubject() + "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.ll_description, R.id.iv_messages, R.id.iv_library, R.id.btn_payment, R.id.btn_mark_as_complete, R.id.ll_add_comments})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_description:
                break;
            case R.id.iv_messages:
                if (entity.getAllowMessage() == 1) {
                    getDockActivity().popFragment();
                    getDockActivity().replaceDockableFragment(CaseMessagesFragment.newInstance(entity), "CaseMessagesFragment");
                } else if (entity.getStatus().equals("3") || entity.getStatus().equals("4")) {

                }else{
                    UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.please_enter_amont));
                }
                break;
            case R.id.iv_library:
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(CaseLibraryFragment.newInstance(entity), "CaseLibraryFragment");
                break;
            case R.id.btn_payment:
                prefHelper.setDuePayment(true);
                getDockActivity().replaceDockableFragment(PaymentFragment.newInstance(entity), "PaymentFragment");
                break;
            case R.id.btn_mark_as_complete:
                if (btnMarkAsComplete.getText().toString().equals(getDockActivity().getResources().getString(R.string.withdraw))) {
                    withdrawCase();
                } else {
                    markAsComplete();
                }
                break;

            case R.id.ll_add_comments:
                final DialogHelper dialogHelper = new DialogHelper(getDockActivity());
                dialogHelper.addComments(R.layout.dialog_add_comments, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                        if (dialogHelper.edtWriteHere().getText().toString().trim().equals("")) {
                            UIHelper.showShortToastInCenter(getDockActivity(), "Please write something to comment.");
                        } else {
                            dialogHelper.hideDialog();
                            updateComments(dialogHelper.edtWriteHere().getText().toString(), v);
                        }
                    }
                });
                dialogHelper.showDialog();
                break;
        }
    }

    private void withdrawCase() {

        final DialogHelper dialogHelper = new DialogHelper(getDockActivity());

        dialogHelper.caseAcknowledge(R.layout.case_withdraw_dialoge, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceHelper.enqueueCall(webService.caseWithdraw(entity.getId()), WITHDRAWCASE);
                dialogHelper.hideDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHelper.hideDialog();
            }
        });

        dialogHelper.setCancelable(false);
        dialogHelper.showDialog();
    }

    private void markAsComplete() {
        serviceHelper.enqueueCall(webService.completeCase(entity.getId()), WebServiceConstants.MARK_COMPLETE);
    }

    private void updateComments(String text, View v) {
        UIHelper.hideSoftKeyboard(getDockActivity(), v);
        serviceHelper.enqueueCall(webService.createComments(entity.getId(), text), WebServiceConstants.NEW_COMMENT);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
     /*       case WebServiceConstants.MY_CASE_COMMENTS:

                getCases = (MyCaseEntity) result;
                commentsEntity = getCases.getComments();
                bindData(commentsEntity);
                break;*/

            case WebServiceConstants.MARK_COMPLETE:
                //  getDockActivity().replaceDockableFragment(MyCaseFragment.newInstance(), "MyCaseFragment");
                getDockActivity().popFragment();
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.marked_as_completed));
                break;

            case WebServiceConstants.NEW_COMMENT:
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                CommentResponseEnt response = (CommentResponseEnt) result;
                adapter.add(response.getComments().get(response.getComments().size() - 1));
                adapter.notifyDataSetChanged();

                lvComments.setSelection(response.getComments().size() - 1);

                txtNoData.setVisibility(View.GONE);
                lvComments.setVisibility(View.VISIBLE);
                break;

            case WITHDRAWCASE:
                // getDockActivity().replaceDockableFragment(MyCaseFragment.newInstance(), "MyCaseFragment");
                getDockActivity().popFragment();
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.withdraw_case));
                break;
        }
    }

    public void bindData(ArrayList<CommentsEntity> comments) {


        if (comments.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvComments.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvComments.setVisibility(View.VISIBLE);
        }

        adapter.clearList();
        lvComments.setAdapter(adapter);
        adapter.addAll(comments);
    }


}
