package com.app.court.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.court.R;
import com.app.court.entities.FilterEnt;
import com.app.court.fragments.ChatFragment;
import com.app.court.fragments.HomeFragment;
import com.app.court.fragments.LoginFragment;
import com.app.court.fragments.NotificationsFragment;
import com.app.court.fragments.SideMenuFragment;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.global.SideMenuChooser;
import com.app.court.global.SideMenuDirection;
import com.app.court.helpers.ScreenHelper;
import com.app.court.helpers.UIHelper;
import com.app.court.interfaces.ImageSetter;
import com.app.court.residemenu.ResideMenu;
import com.app.court.ui.views.TitleBar;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenFile;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ChosenVideo;
import com.kbeanie.imagechooser.api.ChosenVideos;
import com.kbeanie.imagechooser.api.FileChooserListener;
import com.kbeanie.imagechooser.api.FileChooserManager;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kbeanie.imagechooser.api.VideoChooserListener;
import com.kbeanie.imagechooser.api.VideoChooserManager;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends DockActivity implements OnClickListener, ImageChooserListener, FileChooserListener, VideoChooserListener {
    public TitleBar titleBar;
    @BindView(R.id.sideMneuFragmentContainer)
    FrameLayout sideMneuFragmentContainer;
    @BindView(R.id.header_main)
    TitleBar header_main;
    @BindView(R.id.mainFrameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private MainActivity mContext;
    private boolean loading;
    private FileChooserManager fm;
    private VideoChooserManager videoManager;
    private ResideMenu resideMenu;

    private float lastTranslate = 0.0f;

    private String sideMenuType;
    private String sideMenuDirection;

    protected DrawerLayout drawerLayout;
    public SideMenuFragment sideMenuFragment;

    private ImageChooserManager imageChooserManager;
    private int chooserType;
    private String filePath;
    private String originalFilePath;
    private final static String TAG = "ICA";
    private String thumbnailFilePath;
    private String thumbnailSmallFilePath;
    private boolean isActivityResultOver = false;
    private ImageSetter imageSetter;
    private ArrayList<FilterEnt> collection = new ArrayList<>();

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dock);
        ButterKnife.bind(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        titleBar = header_main;
        // setBehindContentView(R.layout.fragment_frame);
        mContext = this;
        Log.i("Screen Density", ScreenHelper.getDensity(this) + "");

        sideMenuType = SideMenuChooser.DRAWER.getValue();
        sideMenuDirection = SideMenuDirection.LEFT.getValue();

        setFilterData();

        if (prefHelper.getPaymentFilter().equals("")) {
            prefHelper.setPaymentFilter(AppConstants.LATEST_TO_OLD);
        }

        settingSideMenu(sideMenuType, sideMenuDirection);

        titleBar.setMenuButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sideMenuType.equals(SideMenuChooser.DRAWER.getValue()) && getDrawerLayout() != null) {
                    if (sideMenuDirection.equals(SideMenuDirection.LEFT.getValue())) {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                } else {
                    resideMenu.openMenu(sideMenuDirection);
                }

            }
        });

        titleBar.setBackButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {

                    popFragment();
                    UIHelper.hideSoftKeyboard(getApplicationContext(),
                            titleBar);
                }
            }
        });

        titleBar.setNotificationButtonListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceDockableFragment(NotificationsFragment.newInstance(), "NotificationsFragment");
            }
        });

        if (savedInstanceState == null)
            initFragment();

    }

    private void setFilterData() {

        collection.add(new FilterEnt("In-Queue Cases", "1"));
        collection.add(new FilterEnt("Active Cases", "2"));
        collection.add(new FilterEnt("In-Active Case", "3"));
        collection.add(new FilterEnt("Withdraw Case", "4"));
        collection.add(new FilterEnt("Completed Case", "5"));
        collection.add(new FilterEnt("Request for Complete", "7"));
        collection.add(new FilterEnt("Show All Cases", "1,2,3,4,5,7"));
    }

    public ArrayList<FilterEnt> getCollection() {
        return collection;
    }

    public void setCollection(ArrayList<FilterEnt> collection) {
        this.collection = collection;
    }


    public View getDrawerView() {
        return getLayoutInflater().inflate(getSideMenuFrameLayoutId(), null);
    }


    public void lockDrawer() {
        try {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    private void settingSideMenu(String type, String direction) {

        if (type.equals(SideMenuChooser.DRAWER.getValue())) {


            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams((int) getResources().getDimension(R.dimen.x300), (int) DrawerLayout.LayoutParams.MATCH_PARENT);


            if (direction.equals(SideMenuDirection.LEFT.getValue())) {
                params.gravity = Gravity.LEFT;
                sideMneuFragmentContainer.setLayoutParams(params);
            } else {
                params.gravity = Gravity.RIGHT;
                sideMneuFragmentContainer.setLayoutParams(params);
            }

            sideMenuFragment = SideMenuFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(getSideMenuFrameLayoutId(), sideMenuFragment).commit();

            drawerLayout.closeDrawers();
        } else {
            resideMenu = new ResideMenu(this);
            resideMenu.attachToActivity(this);
            resideMenu.setMenuListener(getMenuListener());
            resideMenu.setScaleValue(0.52f);

            setMenuItemDirection(direction);
        }
    }

    private void setMenuItemDirection(String direction) {

        if (direction.equals(SideMenuDirection.LEFT.getValue())) {

            SideMenuFragment leftSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(leftSideMenuFragment, "LeftSideMenuFragment", direction);

        } else if (direction.equals(SideMenuDirection.RIGHT.getValue())) {

            SideMenuFragment rightSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(rightSideMenuFragment, "RightSideMenuFragment", direction);

        }

    }

    private int getSideMenuFrameLayoutId() {
        return R.id.sideMneuFragmentContainer;

    }


    public void initFragment() {
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        if (prefHelper.isLogin()) {
            replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
        } else {
            replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String Type = bundle.getString("action_type");
            final String actionId = bundle.getString("action_id");

            if (Type != null && Type.equals(AppConstants.AddMessage)) {
                replaceDockableFragment(ChatFragment.newInstance(actionId), "InboxChatFragment");
            }
        }
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    BaseFragment currFrag = (BaseFragment) manager.findFragmentById(getDockFrameLayoutId());
                    if (currFrag != null) {
                        currFrag.fragmentResume();
                    }
                }
            }
        };

        return result;
    }

    @Override
    public void onLoadingStarted() {

        if (mainFrameLayout != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mainFrameLayout.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            loading = true;
        }
    }

    @Override
    public void onLoadingFinished() {
        mainFrameLayout.setVisibility(View.VISIBLE);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (progressBar != null) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        loading = false;
    }

    @Override
    public void onProgressUpdated(int percentLoaded) {

    }

    @Override
    public int getDockFrameLayoutId() {
        return R.id.mainFrameLayout;
    }

    @Override
    public void onMenuItemActionCalled(int actionId, String data) {

    }

    @Override
    public void setSubHeading(String subHeadText) {

    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void hideHeaderButtons(boolean leftBtn, boolean rightBtn) {
    }

    @Override
    public void onBackPressed() {
        if (loading) {
            UIHelper.showLongToastInCenter(getApplicationContext(),
                    R.string.message_wait);
        } else
            super.onBackPressed();

    }

    @Override
    public void onClick(View view) {

    }

    public void notImplemented() {
        UIHelper.showLongToastInCenter(this, "Will be implemented in future version");
    }

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else {
            // progressBar.setVisibility(View.GONE);
        }
    }*/

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public ResideMenu.OnMenuListener getMenuListener() {
        return menuListener;
    }


    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, getResources().getString(R.string.app_name));
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    private void reinitializeVideoChooser() {
        videoManager = new VideoChooserManager(this, chooserType, getResources().getString(R.string.app_name));
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        videoManager.setExtras(bundle);
        videoManager.setVideoChooserListener(this);
        videoManager.reinitialize(filePath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "Saving Stuff");
        Log.i(TAG, "File Path: " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        outState.putBoolean("activity_result_over", isActivityResultOver);
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        outState.putString("orig", originalFilePath);
        outState.putString("thumb", thumbnailFilePath);
        outState.putString("thumbs", thumbnailSmallFilePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }
            if (savedInstanceState.containsKey("media_path")) {
                filePath = savedInstanceState.getString("media_path");
            }
            if (savedInstanceState.containsKey("activity_result_over")) {
                isActivityResultOver = savedInstanceState.getBoolean("activity_result_over");
                originalFilePath = savedInstanceState.getString("orig");
                thumbnailFilePath = savedInstanceState.getString("thumb");
                thumbnailSmallFilePath = savedInstanceState.getString("thumbs");
            }
        }
        Log.i(TAG, "Restoring Stuff");
        Log.i(TAG, "File Path: " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        Log.i(TAG, "Activity Result Over: " + isActivityResultOver);
        if (isActivityResultOver) {
            //populateData();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, getResources().getString(R.string.app_name));
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pickFile() {
        chooserType = ChooserType.REQUEST_PICK_FILE;
        fm = new FileChooserManager(this, getResources().getString(R.string.app_name));
        fm.setMimeType("application/pdf");
        fm.setFileChooserListener(this);
        try {
            fm.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseVideo() {
        chooserType = ChooserType.REQUEST_PICK_VIDEO;
        videoManager = new VideoChooserManager(this,
                ChooserType.REQUEST_PICK_VIDEO, getResources().getString(R.string.app_name));
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        videoManager.setExtras(bundle);
        videoManager.setVideoChooserListener(this);
        videoManager.clearOldFiles();
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = videoManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, getResources().getString(R.string.app_name));
        imageChooserManager.setImageChooserListener(this);
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "OnActivityResult");
        Log.i(TAG, "File Path : " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);

        if (requestCode == ChooserType.REQUEST_PICK_FILE && resultCode == RESULT_OK) {
            if (fm == null) {
                fm = new FileChooserManager(this);
                fm.setFileChooserListener(this);
            }
            Log.i(TAG, "Probable file size: " + fm.queryProbableFileSize(data.getData(), this));
            fm.submit(requestCode, data);
        }


        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else if (resultCode == RESULT_OK && requestCode == ChooserType.REQUEST_PICK_VIDEO) {
            if (videoManager == null) {
                reinitializeVideoChooser();
            }
            videoManager.submit(requestCode, data);
        }
    }


    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "Chosen Image: O - " + image.getFilePathOriginal());
                Log.i(TAG, "Chosen Image: T - " + image.getFileThumbnail());
                Log.i(TAG, "Chosen Image: Ts - " + image.getFileThumbnailSmall());
                isActivityResultOver = true;
                originalFilePath = image.getFilePathOriginal();
                thumbnailFilePath = image.getFileThumbnail();
                thumbnailSmallFilePath = image.getFileThumbnailSmall();
                //pbar.setVisibility(View.GONE);
                if (image != null) {
                    Log.i(TAG, "Chosen Image: Is not null");

                    imageSetter.setImage(originalFilePath, thumbnailFilePath);
//                    imageSetter.setImage(thumbnailFilePath);
                    //loadImage(imageViewThumbnail, image.getFileThumbnail());
                } else {
                    Log.i(TAG, "Chosen Image: Is null");
                }
            }
        });
    }

    @Override
    public void onFileChosen(final ChosenFile file) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // progressBar.setVisibility(View.INVISIBLE);
                try {
                    imageSetter.setFilePath(file.getFilePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                populateFileDetails(file);
            }
        });
    }

    private void populateFileDetails(ChosenFile file) {
        StringBuffer text = new StringBuffer();
        text.append("File name: " + file.getFileName() + "\n\n");
        text.append("File path: " + file.getFilePath() + "\n\n");
        text.append("Mime type: " + file.getMimeType() + "\n\n");
        text.append("File extn: " + file.getExtension() + "\n\n");
        text.append("File size: " + file.getFileSize() / 1024 + "KB");

        //Toast.makeText(this,text.toString(),Toast.LENGTH_LONG).show();
    }


    //FOr VIdeo =======================================
    @Override
    public void onVideoChosen(final ChosenVideo chosenVideo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Chosen Image: O - " + chosenVideo.getVideoFilePath());
                Log.i(TAG, "Chosen Image: T - " + chosenVideo.getThumbnailPath());
                Log.i(TAG, "Chosen Image: Ts - " + chosenVideo.getThumbnailSmallPath());
                isActivityResultOver = true;
                originalFilePath = chosenVideo.getVideoFilePath();
                thumbnailFilePath = chosenVideo.getThumbnailPath();
                thumbnailSmallFilePath = chosenVideo.getThumbnailSmallPath();
                //pbar.setVisibility(View.GONE);
                if (chosenVideo != null) {
                    Log.i(TAG, "Chosen Image: Is not null");


                    imageSetter.setVideo(originalFilePath, chosenVideo.getExtension(), thumbnailFilePath);
                    //loadImage(imageViewThumbnail, image.getFileThumbnail());
                } else {
                    Log.i(TAG, "Chosen Video: Is null");
                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "OnError: " + reason);
                // pbar.setVisibility(View.GONE);
                UIHelper.showLongToastInCenter(MainActivity.this, reason);

            }
        });
    }

    @Override
    public void onVideosChosen(final ChosenVideos chosenVideos) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "On Images Chosen: " + chosenVideos.size());
                onVideoChosen(chosenVideos.getImage(0));
            }
        });
    }
    //=========================================

    @Override
    public void onImagesChosen(final ChosenImages images) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "On Images Chosen: " + images.size());
                onImageChosen(images.getImage(0));
            }
        });
    }

    public interface ImageSetter {
        public void setImage(String imagePath, String thumbnail);

        public void setFilePath(String filePath) throws IOException;

        public void setVideo(String filePath, String extension, String thumbnail);
    }

    public void setImageSetter(ImageSetter imageSetter) {
        this.imageSetter = imageSetter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
