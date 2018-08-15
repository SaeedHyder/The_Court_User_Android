package com.app.court.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.app.court.PermissionDIspacher.CaseChatFragmentPermissionDispacher;
import com.app.court.R;
import com.app.court.activities.MainActivity;
import com.app.court.entities.CaseMessagesEntity;
import com.app.court.entities.MyCaseEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.BasePreferenceHelper;
import com.app.court.helpers.CameraHelper;
import com.app.court.helpers.UIHelper;
import com.app.court.helpers.Utils;
import com.app.court.interfaces.OnItemCancelClick;
import com.app.court.ui.adapters.ArrayListAdapter;
import com.app.court.ui.adapters.AttachedCaseAdapter;
import com.app.court.ui.adapters.RecyclerViewListAdapter;
import com.app.court.ui.binders.BinderChat;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.CustomRecyclerView;
import com.app.court.ui.views.TitleBar;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.yovenny.videocompress.MediaController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;

import static android.app.Activity.RESULT_OK;

public class CaseMessagesFragment extends BaseFragment implements MainActivity.ImageSetter  {


    @BindView(R.id.iv_description)
    ImageView ivDescription;
    @BindView(R.id.ll_messages)
    LinearLayout llMessages;
    @BindView(R.id.iv_library)
    ImageView ivLibrary;
    @BindView(R.id.ll_upper_tab_bar)
    RelativeLayout llUpperTabBar;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_chat)
    CustomRecyclerView lvChat;
    @BindView(R.id.txtSendMessage)
    AnyEditTextView txtSendMessage;
    @BindView(R.id.btn_attachment)
    ImageView btnAttachment;
    @BindView(R.id.btn_send)
    ImageView btnSend;

    private ArrayList<CaseMessagesEntity> getThread;
    private static String CASE_KEY = "CASE_KEY";
    private MyCaseEntity entity;
    Unbinder unbinder;
    protected BroadcastReceiver broadcastReceiver;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerViewListAdapter adapter;

    ArrayList<FileType> fileStringList = new ArrayList<>();
    ArrayList<FileType> fileStringListShow = new ArrayList<>();
    ArrayList<String> fileExtensionList = new ArrayList<>();
    public static final String APP_DIR = "VideoCompressor";
    public static final String COMPRESSED_VIDEOS_DIR = "/Compressed Videos/";
    public static final String TEMP_DIR = "/Temp/";
    private ArrayList<MultipartBody.Part> fileCollection = new ArrayList<>();
    private Map<String, ArrayList<MultipartBody.Part>> documents = new HashMap<>();
    private Snackbar snackbar;
    private View rootView;
    private String mCurrentPhotoPath;
    private File fileUrl;
    @BindView(R.id.rvAttachment)
    RecyclerView rvAttachedDocs;

    public static CaseMessagesFragment newInstance(MyCaseEntity entity) {
        Bundle args = new Bundle();
        args.putString(CASE_KEY, new Gson().toJson(entity));
        CaseMessagesFragment fragment = new CaseMessagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CaseMessagesFragment newInstance() {
        Bundle args = new Bundle();

        CaseMessagesFragment fragment = new CaseMessagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            CASE_KEY = getArguments().getString(CASE_KEY);
        }
        if (CASE_KEY != null) {
            entity = new Gson().fromJson(CASE_KEY, MyCaseEntity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        initAdapter();
        getMainActivity().setImageSetter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llUpperTabBar.setVisibility(View.VISIBLE);
        getCaseMessages();
        onNotificationReceived();
    }

    private void initAdapter() {
        adapter = new AttachedCaseAdapter(getDockActivity(), new OnItemCancelClick() {
            @Override
            public void itemCrossClick(SubmitCaseFragment.FileType fileItem) {

            }

            @Override
            public void itemCrossClickChat(Object item) {
                FileType fileType=(FileType)item;
                fileStringList.remove(fileType);
                adapter.addAll(fileStringList);
                if (fileStringList.size() > 0) {
                    rvAttachedDocs.setVisibility(View.VISIBLE);
                } else {
                    rvAttachedDocs.setVisibility(View.GONE);
                }
            }

        });
        rvAttachedDocs.setLayoutManager(new GridLayoutManager(getDockActivity(), 3));
        rvAttachedDocs.setAdapter(adapter);
    }

    private void onNotificationReceived() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(AppConstants.REGISTRATION_COMPLETE)) {
                    System.out.println("registration complete");
                    System.out.println(prefHelper.getFirebase_TOKEN());

                } else if (intent.getAction().equals(AppConstants.PUSH_NOTIFICATION)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        String Type = bundle.getString("action_type");
                        final String actionId = bundle.getString("action_id");
                        String receivedId = "";
                        if (Type != null && Type.equals(AppConstants.AddMessage)) {
                            if (prefHelper.getSignUpUser().getId() == Integer.parseInt(entity.getUserId())) {
                                receivedId = entity.getLawyerId();
                            } else {
                                receivedId = entity.getUserId();
                            }
                            getDockActivity().replaceDockableFragment(ChatFragment.newInstance(actionId, entity.getId() + "", receivedId), "InboxChatFragment");

                        }
                    }
                }
            }

        };
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


    @OnClick({R.id.iv_description, R.id.ll_messages, R.id.iv_library, R.id.btn_attachment, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_description:
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(CaseDescriptionFragment.newInstance(entity), "CaseDescriptionFragment");
                break;
            case R.id.ll_messages:
                break;
            case R.id.iv_library:
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(CaseLibraryFragment.newInstance(entity), "CaseLibraryFragment");
                break;
            case R.id.btn_attachment:
                if (fileStringList != null && fileStringList.size() == 5) {
                    UIHelper.showShortToastInCenter(getDockActivity(), "You can't select more than 5 attachments");
                    return;
                }
                if (Utils.doubleClickCheck())
                    CaseChatFragmentPermissionDispacher.getStoragePermissionWithPermissionCheck(CaseMessagesFragment.this);
                break;
            case R.id.btn_send:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        ArrayList<MultipartBody.Part> files = new ArrayList<>();
        ArrayList<MultipartBody.Part> thumbnails = new ArrayList<>();
        ArrayList<MultipartBody.Part> type = new ArrayList<>();
        fileCollection = new ArrayList<>();

        if (fileStringList != null) {
            int index = 0;
            for (FileType fileObj : fileStringList) {


                //For thumbnail
                if (fileObj.getThumbnailfileUrl() != null)
//                    File thumbnailFile = new File("file:///" + fileObj.getFileThumbnail());
                    thumbnails.add(MultipartBody.Part.createFormData("thumb_nail",
                            fileObj.getThumbnailfileUrl().getName(), RequestBody.create(MediaType.parse("multipart/form-data"),
                                    fileObj.getThumbnailfileUrl())));
                fileCollection.add(thumbnails.get(index));

                //For type
                type.add(MultipartBody.Part.createFormData("type", fileExtensionList.get(index)));
                fileCollection.add(type.get(index));

                //For file
                files.add(MultipartBody.Part.createFormData("file",
                        fileObj.getFile().getName(), RequestBody.create(MediaType.parse("multipart/form-data"),
                                fileObj.getFile())));
                fileCollection.add(files.get(index));

                index++;
            }

        }

        documents.put("documents[]", fileCollection);

        if (txtSendMessage.getText().toString() != null && !txtSendMessage.getText().toString().trim().equals("")) {
            String receiverId = "";
            if (String.valueOf(prefHelper.getSignUpUser().getId()).equals(entity.getLawyerId())) {
                receiverId = entity.getUserId();
            } else {
                receiverId = entity.getLawyerId();
            }
            serviceHelper.enqueueCall(webService.sendMsg(entity.getId() + "", receiverId, txtSendMessage.getText().toString()), WebServiceConstants.SEND_MSG);

        } else {
            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.write_your_msg));
        }
    }

    private void getCaseMessages() {
        serviceHelper.enqueueCall(webService.getCaseMessage(entity.getId()), WebServiceConstants.CASE_MESSAGE);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.CASE_MESSAGE:
                getThread = (ArrayList<CaseMessagesEntity>) result;
                bindData(getThread);
                break;

            case WebServiceConstants.SEND_MSG:

                adapter.clearList();
                fileStringList = new ArrayList<>();
                rvAttachedDocs.setVisibility(View.GONE);
                txtSendMessage.setText("");
                getThread = new ArrayList<>();
                getThread = (ArrayList<CaseMessagesEntity>) result;

                lvChat.clearList();
                lvChat.addAll(getThread);

                lvChat.scrollToPosition(getThread.size() - 1);

                break;
        }
    }


    private void bindData(ArrayList<CaseMessagesEntity> threadCollection) {
        linearLayoutManager = new LinearLayoutManager(getDockActivity());
        lvChat.BindRecyclerView(new BinderChat(getDockActivity(), prefHelper), threadCollection,
                linearLayoutManager
                , new DefaultItemAnimator());
        lvChat.scrollToPosition(threadCollection.size() - 1);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getDockActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.PUSH_NOTIFICATION));


    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void getStoragePermission() {
        CameraOptionsSheetDialog();
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForStorage(final permissions.dispatcher.PermissionRequest request) {
        new AlertDialog.Builder(getDockActivity())
                .setMessage(R.string.permission_storage_rationale)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showDeniedForStorage() {
        showSnackBar(getResources().getString(R.string.permission_storage_denied));
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showNeverAskForStorage() {
        showSnackBar(getResources().getString(R.string.permission_storage_neverask));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        CaseChatFragmentPermissionDispacher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void CameraOptionsSheetDialog() {
        final String[] stringItems = {getResources().getString(R.string.drive),
                getResources().getString(R.string.camera),
                getResources().getString(R.string.gallery),
                getResources().getString(R.string.video)
        };
        final ActionSheetDialog dialog = new ActionSheetDialog(getDockActivity(), stringItems, null);
        dialog.title(getResources().getString(R.string.upload_file))
                .titleTextSize_SP(14.5f)
                .cancelText(getResources().getString(android.R.string.cancel))
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        getMainActivity().pickFile();
                        break;
                    case 1:
                        getMainActivity().takePicture();

                        break;
                    case 2:
                        getMainActivity().chooseImage();

                        break;
                    case 3:
                        getMainActivity().chooseVideo();
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    private void showSnackBar(String msg) {
        snackbar = Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG);
        snackbar.setAction("Settings", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInstalledAppDetailsActivity(getDockActivity());
            }
        });
        snackbar.show();
    }

    private void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_ HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "LMS User");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == CameraHelper.CAMERA_REQUEST || requestCode == CameraHelper.GALLERY_REQUEST)) {

            fileUrl = CameraHelper.retrieveAndDisplayPicture(requestCode, resultCode, data, getDockActivity());
//        bitmapFile = null;
        } else if (resultCode == RESULT_OK && requestCode == CameraHelper.VIDEO_REQUEST) {
            CameraHelper.retrieveVideo(requestCode, resultCode, data, getDockActivity());
        }

//        adapter.addAll();
    }

    @Override
    public void setImage(String imagePath, String thumbnail) {
        if (imagePath != null) {
            fileUrl = new File(imagePath);
            File thumbnailfileUrl = new File(thumbnail);
            try {
                fileUrl = new Compressor(getDockActivity()).compressToFile(fileUrl);
                thumbnailfileUrl = new Compressor(getDockActivity()).compressToFile(thumbnailfileUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileStringList.add(new FileType(thumbnail, fileUrl, "", thumbnailfileUrl, AppConstants.PHOTO));
            adapter.addAll(fileStringList);
            if (fileStringList.size() > 0) {
                rvAttachedDocs.setVisibility(View.VISIBLE);
            } else {
                rvAttachedDocs.setVisibility(View.GONE);
            }

            fileExtensionList.add("photo");
            //Toast.makeText(getDockActivity(),imagePath,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setFilePath(String filePath) throws IOException {
        if (filePath != null) {
            fileUrl = new File(filePath);
            File _mFile = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                _mFile = getFile(pdfToBitmap(fileUrl));
            }
            if (_mFile != null) {
                fileStringList.add(new FileType("",
                        fileUrl, "", _mFile, AppConstants.FILE));
//            File file = getFile(pdfToBitmap(fileUrl));
//            ImageLoaderHelper.loadImageWithPicasso(getDockActivity(), "file:///" +file.getAbsolutePath(), imgview);
                adapter.addAll(fileStringList);
                fileExtensionList.add("file");
                if (fileStringList.size() > 0) {
                    rvAttachedDocs.setVisibility(View.VISIBLE);
                } else {
                    rvAttachedDocs.setVisibility(View.GONE);
                }
            } else
                UIHelper.showShortToastInCenter(getDockActivity(), "Pdf is corrupted");
        }
    }

    @Override
    public void setVideo(String thumbnailFilePath, String extension, String thumbnail) {
        if (thumbnailFilePath != null) {
            File thumbnailfileUrl = new File(thumbnail);

            try {
                thumbnailfileUrl = new Compressor(getDockActivity()).compressToFile(thumbnailfileUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try2CreateCompressDir();
            String outPath = Environment.getExternalStorageDirectory()
                    + File.separator
                    + APP_DIR
                    + COMPRESSED_VIDEOS_DIR
                    + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
            new VideoCompressor().execute(thumbnailFilePath, outPath);
            fileUrl = new File(outPath);

            fileStringList.add(new FileType(thumbnail, fileUrl, extension, thumbnailfileUrl, AppConstants.VIDEO));
            adapter.addAll(fileStringList);
            fileExtensionList.add("video");
            if (fileStringList.size() > 0) {
                rvAttachedDocs.setVisibility(View.VISIBLE);
            } else {
                rvAttachedDocs.setVisibility(View.GONE);
            }
            //Toast.makeText(getDockActivity(),imagePath,Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap pdfToBitmap(File pdfFile) {
        Bitmap bitmap = null;

        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

            PdfRenderer.Page page = renderer.openPage(0);

            int width = getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
            int height = getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            //Saving the Bitmap
            OutputStream os = new FileOutputStream("/sdcard/LMSUSER.jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.close();
            // close the page
            page.close();

//            }

            // close the renderer
            renderer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmap;

    }

    public class FileType {
        String fileThumbnail, extension;
        File file, thumbnailfileUrl;
        String type;

        public FileType(String fileThumbnail, File file, String extension, File thumbnailfileUrl, String type) {
            this.fileThumbnail = fileThumbnail;
            this.file = file;
            this.type = type;
            this.extension = extension;
            this.thumbnailfileUrl = thumbnailfileUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public FileType(String fileThumbnail, String extension) {
            this.fileThumbnail = fileThumbnail;
            this.extension = extension;
        }

        public String getFileThumbnail() {
            return fileThumbnail;
        }

        public void setFileThumbnail(String fileThumbnail) {
            this.fileThumbnail = fileThumbnail;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public File getThumbnailfileUrl() {
            return thumbnailfileUrl;
        }

        public void setThumbnailfileUrl(File thumbnailfileUrl) {
            this.thumbnailfileUrl = thumbnailfileUrl;
        }
    }

    public File getFile(Bitmap _bitmap) throws IOException {
        if (_bitmap == null) return null;
        //create a file to write bitmap data
        File f = createImageFile();

//Convert bitmap to byte array
        Bitmap bitmap = _bitmap;
        bitmap.setHasAlpha(true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
//        try {
//            f = new Compressor(getDockActivity()).compressToFile(f);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return f;
    }

    public static void try2CreateCompressDir() {
        File f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + COMPRESSED_VIDEOS_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + TEMP_DIR);
        f.mkdirs();
    }

    class VideoCompressor extends AsyncTask<String, Void, Boolean> {

        private static final String TAG = "VideoCompressor";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            getMainActivity().getProgressBar().show();
//            getMainActivity().getProgressBar().setVisibility(View.VISIBLE);
            getDockActivity().onLoadingStarted();

            Log.d(TAG, "Start video compression");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return MediaController.getInstance().convertVideo(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
//            getMainActivity().getProgressBar().hide();
//            getMainActivity().getProgressBar().setVisibility(View.GONE);

            if (compressed) {
                Log.d(TAG, "Compression successfully!");
                getDockActivity().onLoadingFinished();

            }
        }
    }
}
