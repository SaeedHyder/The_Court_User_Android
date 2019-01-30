package com.app.court.ui.binders;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.court.R;
import com.app.court.activities.DockActivity;
import com.app.court.entities.MediaEntity;
import com.app.court.global.AppConstants;
import com.app.court.helpers.BasePreferenceHelper;
import com.app.court.helpers.DialogHelper;
import com.app.court.helpers.InternetHelper;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.viewbinders.abstracts.ViewBinder;
import com.app.court.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderDisplayMedia extends ViewBinder<MediaEntity> {

    private DockActivity context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper basePreferenceHelper;

    public BinderDisplayMedia(DockActivity context, BasePreferenceHelper basePreferenceHelper) {
        super(R.layout.item_gv_display_media);
        this.context = context;
        this.basePreferenceHelper = basePreferenceHelper;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final MediaEntity entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        imageLoader = ImageLoader.getInstance();

        if (entity.getType().equals(AppConstants.VIDEO)) {
            viewHolder.ivGvGallery.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.ivGvGallery.setImageResource(R.drawable.video_placeholder);
        } else if (entity.getType().equals(AppConstants.DOCS)) {
            viewHolder.ivGvGallery.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.ivGvGallery.setImageResource(R.drawable.pdf_image);
        } else {
            imageLoader.displayImage(entity.getPhoto(), viewHolder.ivGvGallery);
        }

        viewHolder.txtName.setText(entity.getName() + "");
        viewHolder.llRvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
                    if (entity.getType().equals(AppConstants.PHOTO)) {

                        final DialogHelper dialogHelper = new DialogHelper(context);
                        dialogHelper.imageDisplayDialog(R.layout.image_display_dialoge, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogHelper.hideDialog();
                            }
                        }, entity);
                        dialogHelper.showDialog();

                    } else {
                        String url = entity.getPhoto();
                        Intent iOpenUrl = new Intent(Intent.ACTION_VIEW);
                        iOpenUrl.setData(Uri.parse(url));
                        context.startActivity(iOpenUrl);
                    }
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_gv_gallery)
        ImageView ivGvGallery;
        @BindView(R.id.txt_name)
        AnyTextView txtName;
        @BindView(R.id.ll_rv_main)
        LinearLayout llRvMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
