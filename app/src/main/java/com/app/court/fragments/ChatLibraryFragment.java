package com.app.court.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.court.R;
import com.app.court.entities.MediaEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChatLibraryFragment extends BaseFragment {
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_photos)
    AnyTextView tvPhotos;
    @BindView(R.id.iv_videos)
    ImageView ivVideos;
    @BindView(R.id.tv_videos)
    AnyTextView tvVideos;
    @BindView(R.id.iv_documents)
    ImageView ivDocuments;
    @BindView(R.id.tv_docs)
    AnyTextView tvDocs;
    private static ArrayList<MediaEntity> IMAGES=new ArrayList<>();
    private static ArrayList<MediaEntity> VIDEOS=new ArrayList<>();
    private static ArrayList<MediaEntity> DOCS=new ArrayList<>();
    ImageLoader imageLoader;
    Unbinder unbinder;

    public static ChatLibraryFragment newInstance(ArrayList<MediaEntity> images, ArrayList<MediaEntity> video, ArrayList<MediaEntity> docs) {
        Bundle args = new Bundle();
        if (images != null)
            if (!(images.size() <= 0))
                IMAGES = images;
        if (video != null)
            if (!(video.size() <= 0))
                VIDEOS = video;
        if (docs != null)
            if (!(docs.size() <= 0))
                DOCS = docs;
        ChatLibraryFragment fragment = new ChatLibraryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_library, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String photos = "<b><font color='#393939'>All Photos </font>";
        String videos = "<b><font color='#393939'>All Videos </font>";
        String documents = "<b><font color='#393939'>All Documents </font>";

        ivVideos.setImageResource(R.drawable.video_placeholder);
        ivDocuments.setImageResource(R.drawable.pdf_image);

        if (IMAGES != null) {
            if (IMAGES.size() > 0) {

                tvPhotos.setText(Html.fromHtml(photos + "(" + IMAGES.size() + ")"), TextView.BufferType.SPANNABLE);
                imageLoader.displayImage(IMAGES.get(0).getPhoto(), ivImage);
            } else {
                tvPhotos.setText(Html.fromHtml(photos + "(0)"), TextView.BufferType.SPANNABLE);
                imageLoader.displayImage("drawable://" + R.drawable.thumbnail, ivImage);
            }
        }
        if (VIDEOS != null) {
            if (VIDEOS.size() > 0)
                tvVideos.setText(Html.fromHtml(videos + "(" + VIDEOS.size() + ")"), TextView.BufferType.SPANNABLE);
            else
                tvVideos.setText(Html.fromHtml(photos + "(0)"), TextView.BufferType.SPANNABLE);
        }
        if (DOCS != null) {
            if (DOCS.size() > 0)
                tvDocs.setText(Html.fromHtml(documents + "(" + DOCS.size() + ")"), TextView.BufferType.SPANNABLE);
            else
                tvDocs.setText(Html.fromHtml(photos + "(0)"), TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_image, R.id.iv_videos, R.id.iv_documents})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_image:
                if (IMAGES != null)
                    if (IMAGES.size() <= 0) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "No photos to preview");
                    } else {
                        getDockActivity().replaceDockableFragment(DisplayMediaFragment.newInstance(AppConstants.PHOTOS, IMAGES), "DisplayMediaFragment");
                    }
                else
                    UIHelper.showShortToastInCenter(getDockActivity(), "No photos to preview");
                break;
            case R.id.iv_videos:
                if (VIDEOS != null)
                    if (VIDEOS.size() <= 0) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "No video to preview");
                    } else {
                        getDockActivity().replaceDockableFragment(DisplayMediaFragment.newInstance(AppConstants.VIDEOS, VIDEOS), "DisplayMediaFragment");
                    }
                else
                    UIHelper.showShortToastInCenter(getDockActivity(), "No video to preview");
                break;
            case R.id.iv_documents:
                if (DOCS != null)
                    if (DOCS.size() <= 0) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "No documents to preview");
                    } else {
                        getDockActivity().replaceDockableFragment(DisplayMediaFragment.newInstance(AppConstants.DOCS, DOCS), "DisplayMediaFragment");
                    }
                else
                    UIHelper.showShortToastInCenter(getDockActivity(), "No documents to preview");
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.library));
    }
}
