package com.app.court.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.app.court.R;
import com.app.court.entities.CommentsEntity;
import com.app.court.helpers.BasePreferenceHelper;
import com.app.court.helpers.DateHelper;
import com.app.court.ui.viewbinders.abstracts.ViewBinder;
import com.app.court.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCaseDescriptionBinder extends ViewBinder<CommentsEntity> {

    private Context context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;

    public MyCaseDescriptionBinder(Context context, BasePreferenceHelper prefHelper) {
        super(R.layout.item_lv_case_description);
        this.context = context;
        this.preferenceHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(CommentsEntity entity, int position, int grpPosition, View view, Activity activity) {
        imageLoader = ImageLoader.getInstance();
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.tvName.setText(preferenceHelper.getSignUpUser().getFullName()+"");
        viewHolder.tvDescription.setText(entity.getMessage()+"");
        viewHolder.tvDate.setText((DateHelper.getLocalDate(entity.getCreatedAt()) + " | " + (DateHelper.getLocalTime(entity.getCreatedAt()))));
        imageLoader.displayImage(entity.getUserDetail().getImageUrl(), viewHolder.ivImage);
    }

    static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_name)
        AnyTextView tvName;
        @BindView(R.id.tv_date)
        AnyTextView tvDate;
        @BindView(R.id.tv_description)
        AnyTextView tvDescription;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
