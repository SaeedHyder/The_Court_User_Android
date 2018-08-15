package com.app.court.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.court.R;
import com.app.court.entities.CaseLibraryEntity;
import com.app.court.entities.MediaEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CaseMediaFragment extends BaseFragment {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_photos)
    AnyTextView tvPhotos;
    @BindView(R.id.tv_qty_photos)
    AnyTextView tvQtyPhotos;
    @BindView(R.id.ll_photos)
    LinearLayout llPhotos;
    @BindView(R.id.iv_videos)
    ImageView ivVideos;
    @BindView(R.id.tv_videos)
    AnyTextView tvVideos;
    @BindView(R.id.tv_qty)
    AnyTextView tvQty;
    @BindView(R.id.ll_videos)
    LinearLayout llVideos;
    @BindView(R.id.iv_documents)
    ImageView ivDocuments;
    @BindView(R.id.tv_docs)
    AnyTextView tvDocs;
    @BindView(R.id.tv_qty_docs)
    AnyTextView tvQtyDocs;
    @BindView(R.id.ll_docs)
    LinearLayout llDocs;
    private static String CASE_LIBRARY = "CASE_LIBRARY";
    private CaseLibraryEntity entity;
    ImageLoader imageLoader;
    private ArrayList<MediaEntity> images;
    private ArrayList<MediaEntity> video;
    private ArrayList<MediaEntity> docs;
    Unbinder unbinder;

    public static CaseMediaFragment newInstance() {
        Bundle args = new Bundle();

        CaseMediaFragment fragment = new CaseMediaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CaseMediaFragment newInstance(CaseLibraryEntity entity) {
        Bundle args = new Bundle();
        args.putString(CASE_LIBRARY, new Gson().toJson(entity));
        CaseMediaFragment fragment = new CaseMediaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CASE_LIBRARY = getArguments().getString(CASE_LIBRARY);
        }
        if (CASE_LIBRARY != null) {
            entity = new Gson().fromJson(CASE_LIBRARY, CaseLibraryEntity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_media, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageLoader = ImageLoader.getInstance();

        tvQtyPhotos.setText(entity.getPhotos() + "");
        tvQty.setText(entity.getVideos() + "");
        tvQtyDocs.setText(entity.getFiles() + "");

        ivVideos.setImageResource(R.drawable.video_placeholder);
        ivVideos.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivDocuments.setImageResource(R.drawable.pdf_image);
        ivDocuments.setScaleType(ImageView.ScaleType.CENTER_CROP);

     /*   imageLoader.displayImage("drawable://" + R.drawable.thumbnail, ivDocuments);
        imageLoader.displayImage("drawable://" + R.drawable.thumbnail, ivVideos);*/

        if (entity.getDocuments().size() > 0) {

            for (int i = 0; i < entity.getDocuments().size(); i++) {
                if (entity.getDocuments().get(i).getType().equals(AppConstants.PHOTO)) {
                    images = new ArrayList<>();
                    imageLoader.displayImage(entity.getDocuments().get(i).getImageUrl(), ivImage);
                    images.add(new MediaEntity(entity.getDocuments().get(i).getImageUrl(), entity.getDocuments().get(i).getThumbNail(), AppConstants.PHOTO, entity.getCreatedAt()));
                }
                if (entity.getDocuments().get(i).getType().equals(AppConstants.VIDEO)) {
                    video = new ArrayList<>();
                    video.add(new MediaEntity(entity.getDocuments().get(i).getImageUrl(), entity.getDocuments().get(i).getFile(), AppConstants.VIDEO, entity.getCreatedAt()));
                }
                if (entity.getDocuments().get(i).getType().equals(AppConstants.FILE)) {
                    docs = new ArrayList<>();
                    docs.add(new MediaEntity(entity.getDocuments().get(i).getImageUrl(), entity.getDocuments().get(i).getFile(), AppConstants.DOCS, entity.getCreatedAt()));
                }
            }
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(entity.getSubject() + "");
        titleBar.showBackButton();
    }



    @OnClick({R.id.ll_photos, R.id.ll_videos, R.id.ll_docs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_photos:
                if (images != null)
                    if (images.size() <= 0) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "No photos to preview");
                    } else {
                        getDockActivity().replaceDockableFragment(DisplayMediaFragment.newInstance(AppConstants.PHOTOS, images), "DisplayMediaFragment");
                    }
                else
                    UIHelper.showShortToastInCenter(getDockActivity(), "No photos to preview");
                break;
            case R.id.ll_videos:
                if (video != null)
                    if (video.size() <= 0) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "No video to preview");
                    } else {
                        getDockActivity().replaceDockableFragment(DisplayMediaFragment.newInstance(AppConstants.VIDEOS, video), "DisplayMediaFragment");
                    }
                else
                    UIHelper.showShortToastInCenter(getDockActivity(), "No video to preview");
                break;
            case R.id.ll_docs:
                if (docs != null)
                    if (docs.size() <= 0) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "No documents to preview");
                    } else {
                        getDockActivity().replaceDockableFragment(DisplayMediaFragment.newInstance(AppConstants.DOCS, docs), "DisplayMediaFragment");
                    }
                else
                    UIHelper.showShortToastInCenter(getDockActivity(), "No documents to preview");
                break;
        }
    }
}
