package com.app.court.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.court.R;
import com.app.court.activities.DockActivity;
import com.app.court.entities.CaseMessagesEntity;
import com.app.court.entities.MediaEntity;
import com.app.court.fragments.ChatLibraryFragment;
import com.app.court.global.AppConstants;
import com.app.court.helpers.BasePreferenceHelper;
import com.app.court.helpers.DateHelper;
import com.app.court.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.court.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BinderChat extends RecyclerViewBinder<CaseMessagesEntity> {

    private DockActivity dockActivity;
    BasePreferenceHelper basePreferenceHelper;
    private ArrayList<MediaEntity> images;
    private ArrayList<MediaEntity> video;
    private ArrayList<MediaEntity> docs;
    ImageLoader imageLoader;

    public BinderChat(DockActivity dockActivity, BasePreferenceHelper prefHelper) {
        super(R.layout.item_chat);
        this.dockActivity = dockActivity;
        this.basePreferenceHelper = prefHelper;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final CaseMessagesEntity entity, final int position, Object holder, Context context) {

        imageLoader = ImageLoader.getInstance();
        ViewHolder viewHolder = (ViewHolder) holder;

        if (entity != null) {

            if (String.valueOf(basePreferenceHelper.getSignUpUser().getId()).equals(entity.getSenderId())) {
                viewHolder.llSender.setVisibility(View.GONE);
                viewHolder.llReciever.setVisibility(View.VISIBLE);

                viewHolder.tvRightName.setText(R.string.me);
                if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                    viewHolder.txtReceiverChatRight.setVisibility(View.VISIBLE);
                    viewHolder.txtReceiverChatRight.setText(entity.getMessage() + "");
                } else {
                    viewHolder.txtReceiverChatRight.setVisibility(View.GONE);
                }
                viewHolder.tvTimeDateRight.setText(DateHelper.getLocalDateMsg(entity.getCreatedAt()));

                if (entity.getDocumentDetail() != null && entity.getDocumentDetail().size() > 0) {

                    viewHolder.rlRightImage.setVisibility(View.VISIBLE);
                    viewHolder.userCount.setText(entity.getDocumentDetail().size() + "");

                    for (int i = 0; i < entity.getDocumentDetail().size(); i++) {
                        if (entity.getDocumentDetail().get(i).getType().equals(AppConstants.PHOTO)) {
                            imageLoader.displayImage(entity.getDocumentDetail().get(0).getThumbUrl(), viewHolder.ivUser);
                            images = new ArrayList<>();
                            images.add(new MediaEntity(entity.getDocumentDetail().get(i).getImageUrl(), entity.getDocumentDetail().get(i).getThumbNail(), AppConstants.PHOTO, entity.getCreatedAt()));
                        }
                        if (entity.getDocumentDetail().get(i).getType().equals(AppConstants.VIDEO)) {
                            imageLoader.displayImage("drawable://" + R.drawable.video_placeholder, viewHolder.ivUser);
                            video = new ArrayList<>();
                            video.add(new MediaEntity(entity.getDocumentDetail().get(i).getImageUrl(), entity.getDocumentDetail().get(i).getFile(), AppConstants.VIDEO, entity.getCreatedAt()));
                        }
                        if (entity.getDocumentDetail().get(i).getType().equals(AppConstants.FILE)) {
                            imageLoader.displayImage("drawable://" + R.drawable.pdf_image, viewHolder.ivUser);
                            docs = new ArrayList<>();
                            docs.add(new MediaEntity(entity.getDocumentDetail().get(i).getImageUrl(), entity.getDocumentDetail().get(i).getFile(), AppConstants.DOCS, entity.getCreatedAt()));
                        }
                    }

                } else {

                    viewHolder.rlRightImage.setVisibility(View.GONE);
                }

            } else {

                viewHolder.llSender.setVisibility(View.VISIBLE);
                viewHolder.llReciever.setVisibility(View.GONE);

                viewHolder.tvLeftName.setText(entity.getSenderDetail().getFullName() + "");
                if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                    viewHolder.txtSenderChatLeft.setVisibility(View.VISIBLE);
                    viewHolder.txtSenderChatLeft.setText(entity.getMessage() + "");
                } else {
                    viewHolder.txtSenderChatLeft.setVisibility(View.GONE);
                }
                viewHolder.tvTimeDateLeft.setText(DateHelper.getLocalDateMsg(entity.getCreatedAt()));

                if (entity.getDocumentDetail() != null && entity.getDocumentDetail().size() > 0) {

                    viewHolder.rlLeftImage.setVisibility(View.VISIBLE);
                    viewHolder.lawyerCount.setText(entity.getDocumentDetail().size() + "");

                    for (int i = 0; i < entity.getDocumentDetail().size(); i++) {
                        if (entity.getDocumentDetail().get(i).getType().equals(AppConstants.PHOTO)) {
                            imageLoader.displayImage(entity.getDocumentDetail().get(0).getThumbUrl(), viewHolder.ivLawyer);
                            images = new ArrayList<>();
                            images.add(new MediaEntity(entity.getDocumentDetail().get(i).getImageUrl(), entity.getDocumentDetail().get(i).getThumbNail(), AppConstants.PHOTO, entity.getCreatedAt()));
                        }
                        if (entity.getDocumentDetail().get(i).getType().equals(AppConstants.VIDEO)) {
                            imageLoader.displayImage("drawable://" + R.drawable.video_placeholder, viewHolder.ivUser);
                            video = new ArrayList<>();
                            video.add(new MediaEntity(entity.getDocumentDetail().get(i).getImageUrl(), entity.getDocumentDetail().get(i).getFile(), AppConstants.VIDEO, entity.getCreatedAt()));
                        }
                        if (entity.getDocumentDetail().get(i).getType().equals(AppConstants.FILE)) {
                            imageLoader.displayImage("drawable://" + R.drawable.pdf_image, viewHolder.ivUser);
                            docs = new ArrayList<>();
                            docs.add(new MediaEntity(entity.getDocumentDetail().get(i).getImageUrl(), entity.getDocumentDetail().get(i).getFile(), AppConstants.DOCS, entity.getCreatedAt()));
                        }
                    }
                } else {

                    viewHolder.rlLeftImage.setVisibility(View.GONE);
                }

            }

            viewHolder.ivUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dockActivity.replaceDockableFragment(ChatLibraryFragment.newInstance(images, video, docs), "DisplayMediaFragment");

                }
            });

            viewHolder.ivLawyer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dockActivity.replaceDockableFragment(ChatLibraryFragment.newInstance(images, video, docs), "DisplayMediaFragment");

                }
            });

        }
    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_left_name)
        AnyTextView tvLeftName;
        @BindView(R.id.iv_lawyer)
        ImageView ivLawyer;
        @BindView(R.id.lawyer_count)
        AnyTextView lawyerCount;
        @BindView(R.id.rl_leftImage)
        RelativeLayout rlLeftImage;
        @BindView(R.id.txtSenderChatLeft)
        AnyTextView txtSenderChatLeft;
        @BindView(R.id.tv_time_date_left)
        AnyTextView tvTimeDateLeft;
        @BindView(R.id.leftLayoutChild)
        LinearLayout leftLayoutChild;
        @BindView(R.id.leftLayout)
        RelativeLayout leftLayout;
        @BindView(R.id.ll_sender)
        LinearLayout llSender;
        @BindView(R.id.tv_right_name)
        AnyTextView tvRightName;
        @BindView(R.id.iv_user)
        ImageView ivUser;
        @BindView(R.id.user_count)
        AnyTextView userCount;
        @BindView(R.id.rl_rightImage)
        RelativeLayout rlRightImage;
        @BindView(R.id.txtReceiverChatRight)
        AnyTextView txtReceiverChatRight;
        @BindView(R.id.tv_time_date_right)
        AnyTextView tvTimeDateRight;
        @BindView(R.id.rightLayout)
        LinearLayout rightLayout;
        @BindView(R.id.RightLayout)
        RelativeLayout RightLayout;
        @BindView(R.id.ll_reciever)
        LinearLayout llReciever;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
