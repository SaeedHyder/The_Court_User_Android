package com.app.court.ui.binders;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.court.R;
import com.app.court.activities.DockActivity;
import com.app.court.entities.FindLawyerEntity;
import com.app.court.fragments.LawyerProfileFragment;
import com.app.court.ui.viewbinders.abstracts.ViewBinder;
import com.app.court.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderFindLawyer extends ViewBinder<FindLawyerEntity> {

    private DockActivity dockActivity;
    private ImageLoader imageLoader;

    public BinderFindLawyer(DockActivity dockActivity) {
        super(R.layout.item_lv_lawyers);
        this.dockActivity = dockActivity;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final FindLawyerEntity entity, int position, int grpPosition, View view, Activity activity) {
        imageLoader = ImageLoader.getInstance();
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String name = "<font color='#858992'>Field </font><b><font color='#858992'>Breach of Contract</font></b>";
        viewHolder.tvField.setText(Html.fromHtml(name), TextView.BufferType.SPANNABLE);
        String field = "<font color='#858992'>Profession </font><b><font color='#858992'>Lawyer</font></b>";
        viewHolder.tvProfession.setText(Html.fromHtml(field), TextView.BufferType.SPANNABLE);
        String profession = "<font color='#858992'>Experience </font><b><font color='#858992'>0 Years</font></b>";
        viewHolder.tvExperience.setText(Html.fromHtml(profession), TextView.BufferType.SPANNABLE);
        viewHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dockActivity.replaceDockableFragment(LawyerProfileFragment.newInstance(entity), "LawyerProfileFragment");
            }
        });

        imageLoader.displayImage(entity.getImageUrl(), viewHolder.ivImage);
        viewHolder.tvName.setText(entity.getFullName() + "");
        int i;

        StringBuilder commaSeparatedNames = new StringBuilder();
        for (i = 0; i < entity.getSpecializations().size(); i++) {
            if (i == 0) {
                commaSeparatedNames.append(entity.getSpecializations().get(i).getSpecializationDetail().getTitle());
            } else {
                commaSeparatedNames.append(", "+entity.getSpecializations().get(i).getSpecializationDetail().getTitle());
            }
        }
        viewHolder.tvField.setText("Field " + commaSeparatedNames);
        viewHolder.tvProfession.setText("Profession " + entity.getAffilationDetail().getTitle());
        viewHolder.tvExperience.setText("Experience " + entity.getExperienceDetail().getTitle());
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.ll_main)
        LinearLayout llMain;
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_name)
        AnyTextView tvName;
        @BindView(R.id.tv_field)
        AnyTextView tvField;
        @BindView(R.id.tv_profession)
        AnyTextView tvProfession;
        @BindView(R.id.tv_experience)
        AnyTextView tvExperience;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
