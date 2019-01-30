package com.app.court.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.app.court.BuildConfig;
import com.app.court.R;
import com.app.court.activities.MainActivity;
import com.app.court.entities.DocumentArray;
import com.app.court.entities.FindLawyerEntity;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.AppConstants;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.CameraHelper;
import com.app.court.helpers.UIHelper;
import com.app.court.helpers.Utils;
import com.app.court.interfaces.OnItemCancelClick;
import com.app.court.ui.adapters.AttachedDocsAdapter;
import com.app.court.ui.adapters.RecyclerViewListAdapter;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.TitleBar;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
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
import java.util.List;
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
import permissions.dispatcher.PermissionRequest;

import static android.app.Activity.RESULT_OK;


public class SubmitCaseFragment extends BaseFragment implements MainActivity.ImageSetter {
    @BindView(R.id.edt_subject)
    AnyEditTextView edtSubject;
    @BindView(R.id.edt_write_here)
    AnyEditTextView edtWriteHere;
    @BindView(R.id.tv_attachment)
    AnyTextView tvAttachment;
    @BindView(R.id.btn_submit_case)
    Button btnSubmitCase;
    @BindView(R.id.rvAttachedDocs)
    RecyclerView rvAttachedDocs;
    private RecyclerViewListAdapter adapter;
    ArrayList<FileType> fileStringList = new ArrayList<>();
    ArrayList<String> fileExtensionList = new ArrayList<>();
    private static String LAWYER_ENTITY = "LAWYER_ENTITY";
    FindLawyerEntity entity;
    private File fileUrl;
    private static final int CAMERA_REQUEST = 1001;
    private Snackbar snackbar;
    private String mCurrentPhotoPath;
    private View rootView;
    public static final String APP_DIR = "VideoCompressor";
    public static final String COMPRESSED_VIDEOS_DIR = "/Compressed Videos/";

    private ArrayList<MultipartBody.Part> fileCollection = new ArrayList<>();
    private Map<String, ArrayList<MultipartBody.Part>> documents = new HashMap<>();


    Unbinder unbinder;

    public static SubmitCaseFragment newInstance(FindLawyerEntity entity) {
        Bundle args = new Bundle();
        args.putString(LAWYER_ENTITY, new Gson().toJson(entity));
        SubmitCaseFragment fragment = new SubmitCaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            LAWYER_ENTITY = getArguments().getString(LAWYER_ENTITY);
        }
        if (LAWYER_ENTITY != null) {
            entity = new Gson().fromJson(LAWYER_ENTITY, FindLawyerEntity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_case, container, false);
        unbinder = ButterKnife.bind(this, view);
        getMainActivity().setImageSetter(this);
        initAdapter();
        return view;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.submit_cases));
        getTitleBar().showBackButton();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initAdapter() {
        adapter = new AttachedDocsAdapter(getDockActivity(), new OnItemCancelClick() {
            @Override
            public void itemCrossClick(FileType item) {
                fileStringList.remove(item);
                adapter.addAll(fileStringList);
            }

            @Override
            public void itemCrossClickChat(Object fileItem) {

            }
        });
        rvAttachedDocs.setLayoutManager(new GridLayoutManager(getDockActivity(), 3));
        rvAttachedDocs.setAdapter(adapter);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void getStoragePermission() {
        CameraOptionsSheetDialog();
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
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
    void showDeniedForStorage() {
        showSnackBar(getResources().getString(R.string.permission_storage_denied));
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        showSnackBar(getResources().getString(R.string.permission_storage_neverask));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private boolean validate() {
        return edtSubject.testValidity() && edtWriteHere.testValidity();
    }

    @OnClick({R.id.tv_attachment, R.id.btn_submit_case})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_attachment:
                if (fileStringList != null && fileStringList.size() == 5) {
                    UIHelper.showShortToastInCenter(getDockActivity(), "You can't select more than 5 attachments");
                    return;
                }
                //  if (Utils.doubleClickCheck())
                //    SubmitCaseFragmentPermissionsDispatcher.getStoragePermissionWithPermissionCheck(SubmitCaseFragment.this);
                requestCameraPermission();
                break;
            case R.id.btn_submit_case:
                if (validate())
                    if (edtWriteHere.getText().toString().length() < 4) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.msg_alert));
                    } else {
                        if (isValidate() && checkLoading() && com.app.court.helpers.Utils.double2ClickCheck()) {
                            callService();
                        }
                    }
                break;
        }
    }

   /* private ArrayList<Map<String, RequestBody>> getCompressedFiles(ArrayList<FileType> filearray) {
        ArrayList<Map<String, RequestBody>> sample = new ArrayList<>();
        for (int i = 0; i < filearray.size(); i++) {
            Map<String, RequestBody> map = new HashMap<>();


            long time = System.currentTimeMillis();

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), filearray.get(i).getFile());
            RequestBody typeString = RequestBody.create(MediaType.parse("text/plain"), filearray.get(i).getType());
            RequestBody thumbnail = RequestBody.create(MediaType.parse("multipart/form-data"), filearray.get(i).getThumbnailfileUrl());

            map.put("documents[" + i + "][file]", requestFile);
            map.put("documents[" + i + "][type]", typeString);
            map.put("documents[" + i + "][thumb_nail]", thumbnail);
            sample.add(i, map);
        }
        return sample;
    }*/

    private void callService() {

        ArrayList<MultipartBody.Part> files = new ArrayList<>();
        ArrayList<MultipartBody.Part> thumbnails = new ArrayList<>();
        ArrayList<MultipartBody.Part> type = new ArrayList<>();


        if (fileStringList != null) {
            int index = 0;
            for (FileType fileObj : fileStringList) {

                if (fileObj.getFile() != null) {
                    files.add(MultipartBody.Part.createFormData("file[]",
                            fileObj.getFile().getName(), RequestBody.create(MediaType.parse("multipart/form-data"),
                                    fileObj.getFile())));
                }

                if (fileObj.getThumbnailfileUrl() != null) {
                    thumbnails.add(MultipartBody.Part.createFormData("thumb_nail[]",
                            fileObj.getThumbnailfileUrl().getName(), RequestBody.create(MediaType.parse("multipart/form-data"),
                                    fileObj.getThumbnailfileUrl())));
                }

                type.add(MultipartBody.Part.createFormData("type[]", fileStringList.get(index).getType()));
               // type.add(MultipartBody.Part.createFormData("type[]", fileExtensionList.get(index)));
                index++;
            }
        }

            RequestBody lawyer_id = RequestBody.create(MediaType.parse("text/plain"), entity.getId() + "");
            RequestBody subject = RequestBody.create(MediaType.parse("text/plain"), edtSubject.getText().toString() + "");
            RequestBody detail = RequestBody.create(MediaType.parse("text/plain"), edtWriteHere.getText().toString() + "");

            serviceHelper.enqueueCall(webService.submitCase(lawyer_id, subject, detail, files, thumbnails, type), WebServiceConstants.SUBMIT_CASE);

    }

    private ArrayList<DocumentArray> getDocumentArray(ArrayList<FileType> fileStringList) {
        ArrayList<DocumentArray> docArray = new ArrayList<>();

        for (FileType item : fileStringList) {
            docArray.add(new DocumentArray(item.getFile(), item.getType(), item.getThumbnailfileUrl()));
        }

        return docArray;
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);
        switch (Tag) {
            case WebServiceConstants.SUBMIT_CASE:
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.submit_case_successfully));
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                break;
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        SubmitCaseFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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

    public static final String TEMP_DIR = "/Temp/";


    public static void try2CreateCompressDir() {
        File f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + COMPRESSED_VIDEOS_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + TEMP_DIR);
        f.mkdirs();
    }

    private boolean isValidate() {

        if (edtSubject.getText().toString() == null || edtSubject.getText().toString().trim().equals("")) {
            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.write_subject));
            return false;
        } else if (edtWriteHere.getText().toString() == null || edtWriteHere.getText().toString().trim().equals("")) {
            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.write_description));
            return false;
        } else if (fileStringList.size() <= 0) {
            UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.upload_doc));
            return false;
        }else{
            return true;
        }

    }



    protected void fillAllFieldsError() {
        UIHelper.showLongToastInCenter(getActivity(), "Please fill all fields");
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

    //Camera Integration

    public void uploadFromCamera() {
        Intent pictureActionIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI;
        try {
            photoURI = FileProvider.getUriForFile(getDockActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    createImageFile());
            if (photoURI != null) {
                pictureActionIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                SubmitCaseFragment.this.startActivityForResult(pictureActionIntent, CAMERA_REQUEST);
            }
        } catch (IOException e) {
            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.failure_response));
            e.printStackTrace();
        }
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

            fileExtensionList.add("photo");
            //Toast.makeText(getDockActivity(),imagePath,Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setFilePath(String filePath) throws IOException {
        if (filePath != null) {
            fileUrl = new File(filePath);
            File _mFile = getFile(pdfToBitmap(fileUrl));
            if (_mFile != null) {
                fileStringList.add(new FileType("",
                        fileUrl, "", _mFile, AppConstants.FILE));
//            File file = getFile(pdfToBitmap(fileUrl));
//            ImageLoaderHelper.loadImageWithPicasso(getDockActivity(), "file:///" +file.getAbsolutePath(), imgview);
                adapter.addAll(fileStringList);
                fileExtensionList.add("file");
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

    private void requestCameraPermission() {
        Dexter.withActivity(getDockActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            // SubmitCaseFragmentPermissionsDispatcher.getStoragePermissionWithPermissionCheck(ChatFragment.this);
                            CameraOptionsSheetDialog();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            requestCameraPermission();

                        } else if (report.getDeniedPermissionResponses().size() > 0) {
                            requestCameraPermission();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "Grant Camera and Storage Permission to processed");
                        openSettings();
                    }
                })

                .onSameThread()
                .check();


    }

    private void openSettings() {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        Uri uri = Uri.fromParts("package", getDockActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
