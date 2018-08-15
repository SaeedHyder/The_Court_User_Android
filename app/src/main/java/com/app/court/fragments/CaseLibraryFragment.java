package com.app.court.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.court.R;
import com.app.court.entities.MediaEntity;
import com.app.court.entities.MyCaseEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.binders.BinderCaseLibrary;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.google.gson.Gson;
import com.lopei.collageview.CollageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CaseLibraryFragment extends BaseFragment {
    @BindView(R.id.iv_description)
    ImageView ivDescription;
    @BindView(R.id.iv_messages)
    ImageView ivMessages;
    @BindView(R.id.ll_library)
    LinearLayout llLibrary;
    @BindView(R.id.ll_upper_tab_bar)
    RelativeLayout llUpperTabBar;
    @BindView(R.id.iv_videos)
    ImageView ivVideos;
    @BindView(R.id.iv_documents)
    ImageView ivDocuments;
    @BindView(R.id.lv_library)
    ListView lvLibrary;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.tv_photos)
    AnyTextView tvPhotos;
    @BindView(R.id.tv_videos)
    AnyTextView tvVideos;
    @BindView(R.id.tv_docs)
    AnyTextView tvDocs;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    private ArrayList<String> collection;
    private ArrayListAdapter<String> adapter;
    private ArrayList<String> arrayListImageCollection;
    private ArrayList<MediaEntity> images;
    private ArrayList<MediaEntity> video;
    private ArrayList<MediaEntity> docs;
    CollageView.IconSelector iconSelector;
    private int[] resIds;
    private ImageLoader imageLoader;
    private static String CASE_KEY = "CASE_KEY";
    private MyCaseEntity entity;
    Unbinder unbinder;

    public static CaseLibraryFragment newInstance(MyCaseEntity entity) {
        Bundle args = new Bundle();
        args.putString(CASE_KEY, new Gson().toJson(entity));
        CaseLibraryFragment fragment = new CaseLibraryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CaseLibraryFragment newInstance() {
        Bundle args = new Bundle();

        CaseLibraryFragment fragment = new CaseLibraryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adapter = new ArrayListAdapter<>(getDockActivity(), new BinderCaseLibrary(getDockActivity(), prefHelper));
        imageLoader = ImageLoader.getInstance();
        if (getArguments() != null) {
            CASE_KEY = getArguments().getString(CASE_KEY);
        }
        if (CASE_KEY != null) {
            entity = new Gson().fromJson(CASE_KEY, MyCaseEntity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_library, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (entity.getAllowMessage() == 1) {
            ivMessages.setImageResource(R.drawable.messages);
        } else {
            ivMessages.setImageResource(R.drawable.disable);
        }

        imageLoader = ImageLoader.getInstance();
        llUpperTabBar.setVisibility(View.VISIBLE);
        lvLibrary.setVisibility(View.GONE);

        String photos = "<b><font color='#393939'>All Photos </font>";
        tvPhotos.setText(Html.fromHtml(photos + entity.getPhotos()), TextView.BufferType.SPANNABLE);
        String videos = "<b><font color='#393939'>All Videos </font>";
        tvVideos.setText(Html.fromHtml(videos + entity.getVideos()), TextView.BufferType.SPANNABLE);
        String documents = "<b><font color='#393939'>All Documents </font>";
        tvDocs.setText(Html.fromHtml(documents + entity.getFiles()), TextView.BufferType.SPANNABLE);

        ivVideos.setImageResource(R.drawable.video_placeholder);
        ivDocuments.setImageResource(R.drawable.pdf_image);
        if(entity.getDocuments().size()>0){
            imageLoader.displayImage(entity.getDocuments().get(0).getThumbUrl(), ivImage);

            for (int i = 0; i < entity.getDocuments().size(); i++) {
                if (entity.getDocuments().get(i).getType().equals(AppConstants.PHOTO)) {
                    images = new ArrayList<>();
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
            }}
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(MyCaseFragment.newInstance(), "MyCaseFragment");
            }
        });
        titleBar.setSubHeading(entity.getSubject() + "");
    }



    @OnClick({R.id.iv_description, R.id.iv_messages, R.id.ll_library, R.id.iv_image, R.id.iv_videos, R.id.iv_documents})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_description:
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(CaseDescriptionFragment.newInstance(entity), "CaseDescriptionFragment");
                break;
            case R.id.iv_messages:
                if (entity.getAllowMessage() == 1) {
                    getDockActivity().popFragment();
                    getDockActivity().replaceDockableFragment(CaseMessagesFragment.newInstance(entity), "CaseMessagesFragment");
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), "Please submit amount to proceed.");
                }
                break;
            case R.id.ll_library:
                break;
            case R.id.iv_image:
                if (images != null)
                    if (images.size() <= 0) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "No photos to preview");
                    } else {
                        getDockActivity().replaceDockableFragment(DisplayMediaFragment.newInstance(AppConstants.PHOTOS, images), "DisplayMediaFragment");
                    }
                else
                    UIHelper.showShortToastInCenter(getDockActivity(), "No photos to preview");
                break;
            case R.id.iv_videos:
                if (video != null)
                    if (video.size() <= 0) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "No video to preview");
                    } else {
                        getDockActivity().replaceDockableFragment(DisplayMediaFragment.newInstance(AppConstants.VIDEOS, video), "DisplayMediaFragment");
                    }
                else
                    UIHelper.showShortToastInCenter(getDockActivity(), "No video to preview");
                break;
            case R.id.iv_documents:
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
