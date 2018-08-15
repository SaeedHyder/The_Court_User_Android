package com.app.court.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.app.court.R;
import com.app.court.activities.DockActivity;
import com.app.court.entities.MessageThreadEntity;
import com.app.court.fragments.ChatFragment;
import com.app.court.helpers.BasePreferenceHelper;
import com.app.court.helpers.DateHelper;
import com.app.court.ui.viewbinders.abstracts.ViewBinder;
import com.app.court.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BinderMessagesThread extends ViewBinder<MessageThreadEntity> {

    private DockActivity context;
    private ImageLoader imageLoader;
    private String receiverId;
    private BasePreferenceHelper preferenceHelper;

    public BinderMessagesThread(DockActivity context,BasePreferenceHelper preferenceHelper) {
        super(R.layout.item_lv_messages);
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        this.preferenceHelper=preferenceHelper;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final MessageThreadEntity entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (entity != null) {
            if (entity.getCaseDetail() != null) {

                if (entity.getCaseDetail().getSubject() != null)
                    viewHolder.tvName.setText(entity.getCaseDetail().getSubject() + "");
                if (entity.getMessage() != null)
                    viewHolder.tvDescription.setText(entity.getMessage() + "");
                if (entity.getCreatedAt() != null)
                    viewHolder.tvDate.setText(DateHelper.getLocalDate(entity.getCreatedAt()));
                if (entity.getCreatedAt() != null)
                    viewHolder.tvTime.setText(DateHelper.getLocalTime(entity.getCreatedAt()));
            }
        }

        if(entity.getSenderId().equals(String.valueOf(preferenceHelper.getSignUpUser().getId()))){
            receiverId=entity.getReceiverId();
        }else{
            receiverId=entity.getSenderId();
        }

        viewHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.replaceDockableFragment(ChatFragment.newInstance(entity.getId() + "", entity.getCaseId(),receiverId), "ChatFragment");
            }
        });
    }


    static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.imageView)
        CircleImageView imageView;
        @BindView(R.id.tv_name)
        AnyTextView tvName;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;
        @BindView(R.id.tv_date)
        AnyTextView tvDate;
        @BindView(R.id.tv_time)
        AnyTextView tvTime;
        @BindView(R.id.ll_main)
        LinearLayout llMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
