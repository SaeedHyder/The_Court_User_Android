package com.app.court.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.court.R;
import com.app.court.entities.MyCaseEntity;
import com.app.court.helpers.BasePreferenceHelper;
import com.app.court.helpers.DateHelper;
import com.app.court.interfaces.CaseItemClick;
import com.app.court.ui.viewbinders.abstracts.ViewBinder;
import com.app.court.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCaseBinder extends ViewBinder<MyCaseEntity> {

    private Context context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;
    private CaseItemClick caseItemClick;

    public MyCaseBinder(Context context, BasePreferenceHelper prefHelper,CaseItemClick caseItemClick) {
        super(R.layout.row_item_mycase);
        this.context = context;
        this.preferenceHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
        this.caseItemClick=caseItemClick;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final MyCaseEntity entity, final int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.tvTitle.setText(entity.getSubject() + "");
        viewHolder.tvDescription.setText(entity.getDetail() + "");
        viewHolder.tvMessages.setText("Messages: " + entity.getComments().size());

        if (entity.getStatus().equals("1")) {
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.app_dark_gray));
            viewHolder.tvStatus.setText(R.string.inqueue);
        } else if (entity.getStatus().equals("2")) {
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.app_orange));
            viewHolder.tvStatus.setText(R.string.active);
        } else if (entity.getStatus().equals("3")) {
            viewHolder.tvStatus.setText(R.string.inactive);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
        } else if (entity.getStatus().equals("4")) {
            viewHolder.tvStatus.setText(R.string.withdraw);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
        } else if (entity.getStatus().equals("5")) {
            viewHolder.tvStatus.setText(R.string.completed);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
        } else if (entity.getStatus().equals("7")) {
            viewHolder.tvStatus.setText(R.string.acknowledge);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
        } else if (entity.getStatus().equals("1,2,3,4,5,7")) {
            viewHolder.tvStatus.setText("Show All Case");
        }

        viewHolder.tvDate.setText((DateHelper.getLocalDate(entity.getCreatedAt()) + " | " + (DateHelper.getLocalTime(entity.getCreatedAt()))));
        String specialization = "<b><font color='#4F4F4F'>Lawyer: </font></b>";
        viewHolder.tvLaywerName.setText(Html.fromHtml(specialization + entity.getLawyerDetail().getFullName() + ""), TextView.BufferType.SPANNABLE);

        viewHolder.mainFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caseItemClick.onClick(entity,position);
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_title)
        AnyTextView tvTitle;
        @BindView(R.id.tv_date)
        AnyTextView tvDate;
        @BindView(R.id.tv_laywer_name)
        AnyTextView tvLaywerName;
        @BindView(R.id.tv_messages)
        AnyTextView tvMessages;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;
        @BindView(R.id.tv_status)
        AnyTextView tvStatus;
        @BindView(R.id.mainFrame)
        LinearLayout mainFrame;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
