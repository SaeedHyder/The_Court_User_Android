package com.app.court.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.court.R;
import com.app.court.activities.DockActivity;
import com.app.court.entities.CaseLibraryEntity;
import com.app.court.entities.DocumentEntity;
import com.app.court.fragments.CaseMediaFragment;
import com.app.court.global.AppConstants;
import com.app.court.helpers.BasePreferenceHelper;
import com.app.court.ui.viewbinders.abstracts.ViewBinder;
import com.app.court.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderCaseLibrary extends ViewBinder<CaseLibraryEntity> {

    private DockActivity dockActivity;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;
    private boolean isImage ;

    public BinderCaseLibrary(DockActivity dockActivity, BasePreferenceHelper prefHelper) {
        super(R.layout.item_lv_library);
        this.dockActivity = dockActivity;
        this.preferenceHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final CaseLibraryEntity entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.tvName.setText(entity.getSubject() + "");
        viewHolder.tvPhotos.setText(entity.getPhotos() + "");
        viewHolder.tvVideos.setText(entity.getVideos() + "");
        viewHolder.tvDocuments.setText(entity.getFiles() + "");
        isImage = false;

        if (entity != null && entity.getDocuments() != null && entity.getDocuments().size() > 0 && entity.getDocuments().get(0) != null && entity.getDocuments().get(0).getType() != null) {

            for (DocumentEntity item : entity.getDocuments()) {
                if (item.getType().equals(AppConstants.PHOTO)) {
                    viewHolder.ivImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageLoader.displayImage(item.getImageUrl(), viewHolder.ivImage);
                    isImage = true;
                }
            }
            if (!isImage) {
                if (entity.getDocuments().get(0).getType().equals(AppConstants.VIDEO)) {
                    viewHolder.ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    viewHolder.ivImage.setImageResource(R.drawable.video_placeholder);
                } else if (entity.getDocuments().get(0).getType().equals(AppConstants.FILE)) {
                    viewHolder.ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    viewHolder.ivImage.setImageResource(R.drawable.pdf_image);
                }
            }

        } else {
            viewHolder.ivImage.setImageResource(R.drawable.thumbnail);
        }
        viewHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dockActivity.replaceDockableFragment(CaseMediaFragment.newInstance(entity), "CaseMediaFragment");
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.ll_main)
        LinearLayout llMain;
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_name)
        AnyTextView tvName;
        @BindView(R.id.tv_photos)
        AnyTextView tvPhotos;
        @BindView(R.id.tv_videos)
        AnyTextView tvVideos;
        @BindView(R.id.tv_documents)
        AnyTextView tvDocuments;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
