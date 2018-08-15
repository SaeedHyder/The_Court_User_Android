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
import com.app.court.entities.CaseLibraryEntity;
import com.app.court.entities.DocumentEntity;
import com.app.court.entities.LibraryEntity;
import com.app.court.entities.MediaEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.binders.BinderCaseLibrary;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class LibraryFragment extends BaseFragment {
    @BindView(R.id.iv_description)
    ImageView ivDescription;
    @BindView(R.id.iv_messages)
    ImageView ivMessages;
    @BindView(R.id.ll_library)
    LinearLayout llLibrary;
    @BindView(R.id.ll_upper_tab_bar)
    RelativeLayout llUpperTabBar;
    @BindView(R.id.iv_image)
    ImageView ivImage;
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
    ImageLoader imageLoader;
    @BindView(R.id.mainFrame)
    LinearLayout mainFrame;

    private ArrayList<DocumentEntity> entity;
    private ArrayList<MediaEntity> images = new ArrayList<>();
    private ArrayList<MediaEntity> video = new ArrayList<>();
    private ArrayList<MediaEntity> docs = new ArrayList<>();

    LibraryEntity libraryEntity;
    private ArrayList<CaseLibraryEntity> userCollection;
    private ArrayListAdapter<CaseLibraryEntity> adapter;

    Unbinder unbinder;

    public static LibraryFragment newInstance() {

        Bundle args = new Bundle();
        LibraryFragment fragment = new LibraryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<>(getDockActivity(), new BinderCaseLibrary(getDockActivity(), prefHelper));
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
        images.clear();
        video.clear();
        docs.clear();
        llUpperTabBar.setVisibility(View.GONE);
        lvLibrary.setVisibility(View.GONE);
        imageLoader = ImageLoader.getInstance();
        mainFrame.setVisibility(View.GONE);
        getAllLibrary();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading("Library");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_description, R.id.iv_messages, R.id.ll_library, R.id.iv_image, R.id.iv_videos, R.id.iv_documents})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_description:
                //getDockActivity().replaceDockableFragment(CaseDescriptionFragment.newInstance(), "CaseDescriptionFragment");
                break;
            case R.id.iv_messages:
                //getDockActivity().replaceDockableFragment(CaseMessagesFragment.newInstance(), "CaseMessagesFragment");
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

    private void getAllLibrary() {
        serviceHelper.enqueueCall(webService.getLibrary(), WebServiceConstants.ALL_LIBRARY);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.ALL_LIBRARY:

                mainFrame.setVisibility(View.VISIBLE);

                libraryEntity = (LibraryEntity) result;
                entity = libraryEntity.getLibaray();
                if (!(entity.size() <= 0)) {
                    setUpperData();
                }
                userCollection = libraryEntity.getCases();

                if (userCollection.size() <= 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                    lvLibrary.setVisibility(View.GONE);
                } else {
                    txtNoData.setVisibility(View.GONE);
                    lvLibrary.setVisibility(View.VISIBLE);
                    bindData(userCollection);
                }

                String photos = "<b><font color='#393939'>All Photos </font></b>";
                tvPhotos.setText(Html.fromHtml(photos + libraryEntity.getPhotos() + ""), TextView.BufferType.SPANNABLE);
                String videos = "<b><font color='#393939'>All Videos </font></b>";
                tvVideos.setText(Html.fromHtml(videos + libraryEntity.getVideos() + ""), TextView.BufferType.SPANNABLE);
                String documents = "<b><font color='#393939'>All Documents </font></b>";
                tvDocs.setText(Html.fromHtml(documents + libraryEntity.getFiles() + ""), TextView.BufferType.SPANNABLE);
                break;
        }
    }

    private void bindData(ArrayList<CaseLibraryEntity> caseLibraryCollection) {
        adapter.clearList();
        lvLibrary.setAdapter(adapter);
        adapter.addAll(caseLibraryCollection);

    }

    private void setUpperData() {

        imageLoader.displayImage(entity.get(0).getThumbUrl(), ivImage);
        ivVideos.setImageResource(R.drawable.video_placeholder);
        ivDocuments.setImageResource(R.drawable.pdf_image);

        for (int i = 0; i < entity.size(); i++) {
            if (entity.get(i).getType().equals(AppConstants.PHOTO)) {
                images.add(new MediaEntity(entity.get(i).getThumbUrl(), entity.get(i).getThumbNail(), AppConstants.PHOTO, entity.get(i).getCreatedAt()));
            }
            if (entity.get(i).getType().equals(AppConstants.VIDEO)) {
                video.add(new MediaEntity(entity.get(i).getImageUrl(), entity.get(i).getFile(), AppConstants.VIDEO, entity.get(i).getCreatedAt()));
            }
            if (entity.get(i).getType().equals(AppConstants.FILE)) {
                docs.add(new MediaEntity(entity.get(i).getImageUrl(), entity.get(i).getFile(), AppConstants.DOCS, entity.get(i).getCreatedAt()));
            }
        }
    }
}
