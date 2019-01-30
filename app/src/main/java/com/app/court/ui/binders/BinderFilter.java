package com.app.court.ui.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.app.court.R;
import com.app.court.activities.DockActivity;
import com.app.court.entities.FilterEnt;
import com.app.court.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.court.ui.viewbinders.abstracts.ViewBinder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderFilter extends RecyclerViewBinder<FilterEnt> {

    private DockActivity context;

    public BinderFilter(DockActivity context) {
        super(R.layout.item_lv_filter);
        this.context = context;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final FilterEnt entity, final int position, Object holder, Context context) {

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.cbFilterTypes.setText(entity.getData() + "");

            if (entity.isChecked()) {
                viewHolder.cbFilterTypes.setChecked(true);
            } else {
                viewHolder.cbFilterTypes.setChecked(false);
            }


        viewHolder.cbFilterTypes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                entity.setChecked(isChecked);
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.cb_filter_types)
        CheckBox cbFilterTypes;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
