package com.app.court.ui.binders;

import android.app.Activity;
import android.view.View;

import com.app.court.R;
import com.app.court.activities.DockActivity;
import com.app.court.entities.PaymentEntity;
import com.app.court.helpers.DateHelper;
import com.app.court.ui.viewbinders.abstracts.ViewBinder;
import com.app.court.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderPayments extends ViewBinder<PaymentEntity> {

    private DockActivity dockActivity;

    public BinderPayments(DockActivity dockActivity) {
        super(R.layout.item_payment);
        this.dockActivity = dockActivity;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(PaymentEntity entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.tvCaseType.setText(entity.getTitle());
        viewHolder.tvAmount.setText("$ " + entity.getCharges());
        viewHolder.tvDate.setText(DateHelper.getLocalDate(entity.getCreatedAt()) + " | " + DateHelper.getLocalTime(entity.getCreatedAt()));

    }

    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_case_type)
        AnyTextView tvCaseType;
        @BindView(R.id.tv_date)
        AnyTextView tvDate;
        @BindView(R.id.tv_amount)
        AnyTextView tvAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
