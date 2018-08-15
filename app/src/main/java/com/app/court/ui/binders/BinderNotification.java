package com.app.court.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.app.court.R;
import com.app.court.entities.NotificationEnt;
import com.app.court.entities.NotificationEntity;
import com.app.court.global.AppConstants;
import com.app.court.helpers.BasePreferenceHelper;
import com.app.court.helpers.DateHelper;
import com.app.court.ui.viewbinders.abstracts.ViewBinder;
import com.app.court.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderNotification extends ViewBinder<NotificationEntity> {

    private Context context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;
    String splittedDate;
    String splittedTime;

    public BinderNotification(Context context, BasePreferenceHelper prefHelper) {
        super(R.layout.row_item_notification);
        this.context = context;
        this.preferenceHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(NotificationEntity entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.tvMsg.setText(entity.getMessage() + "");
        viewHolder.tvDate.setText(DateHelper.getLocalDateNotification(entity.getCreatedAt()));
    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_msg)
        AnyTextView tvMsg;
        @BindView(R.id.tv_date)
        AnyTextView tvDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
