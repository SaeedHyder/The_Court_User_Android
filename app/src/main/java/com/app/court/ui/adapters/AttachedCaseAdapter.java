package com.app.court.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.court.R;
import com.app.court.fragments.CaseMessagesFragment;
import com.app.court.interfaces.OnItemCancelClick;
import com.nostra13.universalimageloader.core.ImageLoader;


public class AttachedCaseAdapter extends RecyclerViewListAdapter<CaseMessagesFragment.FileType> {
    private Context context;
    OnItemCancelClick onItemCancelClick;

    public AttachedCaseAdapter(Context context, OnItemCancelClick onItemCancelClick) {
        super(context);
        this.context = context;
        this.onItemCancelClick = onItemCancelClick;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item_attached_docs, viewGroup, false);
    }


    @Override
    protected void bindView(final CaseMessagesFragment.FileType item, RecyclerviewViewHolder viewHolder) {
        if (item != null) {
            ImageView imageView = (ImageView) viewHolder.getView(R.id.imgview);
            ImageView imgPlaceHolder = (ImageView) viewHolder.getView(R.id.imgPlaceHolder);
            ImageView imgCross = (ImageView) viewHolder.getView(R.id.imgCross);

            if (item.getFileThumbnail() != null && !item.getFileThumbnail().isEmpty())
                ImageLoader.getInstance().displayImage("file:///" + item.getFileThumbnail(), imageView);
            else
                ImageLoader.getInstance().displayImage("file:///" + item.getThumbnailfileUrl().getPath(), imageView);

            if (item.getExtension().equals("mp4")) {
                imgPlaceHolder.setVisibility(View.VISIBLE);
            } else
                imgPlaceHolder.setVisibility(View.INVISIBLE);


            imgCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemCancelClick.itemCrossClickChat(item);
                }
            });

        }
    }

    @Override
    protected int bindItemViewType(int position) {
        return 0;
    }

    @Override
    protected int bindItemId(int position) {
        return 0;
    }
}


