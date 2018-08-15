package com.app.court.helpers;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.app.court.activities.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CameraHelper {

	private static Intent pictureActionIntent;

	public static final int CAMERA_REQUEST = 1001;
	public static final int GALLERY_REQUEST = 1002;
	public static final int VIDEO_REQUEST = 1003;

	private static String image_path_string = "";

	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;

	private static Uri uriImage;

//	@Nullable
	private static File picture = null;
	
//	@Nullable
	private static File video = null;

	public static void uploadFromGallery(Fragment fragment) {

		pictureActionIntent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		pictureActionIntent.setType("image/*");
		pictureActionIntent.putExtra("return-data", false);
		fragment.startActivityForResult(pictureActionIntent, GALLERY_REQUEST);

	}

	public static void uploadVideo(Fragment fragment) {

		pictureActionIntent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		pictureActionIntent.setType("video/*");
		pictureActionIntent.putExtra("return-data", false);
		fragment.startActivityForResult(pictureActionIntent, VIDEO_REQUEST);

	}

	public static void uploadFromGalleryAndVideo(Fragment fragment) {

		pictureActionIntent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		pictureActionIntent.setType("video/*, images/*");
		pictureActionIntent.putExtra("return-data", false);
		fragment.startActivityForResult(pictureActionIntent, GALLERY_REQUEST);

	}

	// public static void uploadMultipleFromGallery(Fragment fragment) {
	//
	// pictureActionIntent = new Intent(Intent.ACTION_PICK,
	// MediaStore.Images.Media.INTERNAL_CONTENT_URI);
	// pictureActionIntent.setType("image/*");
	// pictureActionIntent.putExtra("return-data", false);
	// pictureActionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
	// fragment.startActivityForResult(pictureActionIntent, GALLERY_REQUEST);
	//
	// }

	public static void uploadFromCamera(Fragment fragment) {

		pictureActionIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/iTravel/";
		uriImage = Uri.fromFile(new File(path
				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
		Funcs.addImageParamsExtra(pictureActionIntent, uriImage);
		fragment.startActivityForResult(pictureActionIntent, CAMERA_REQUEST);

	}

	public static File retrievePicture(int requestCode, int resultCode,
			Intent data, Activity activity) {

		try {
			Uri selectedImageUri = data.getData();
			image_path_string = Funcs.getPath(selectedImageUri, activity);
			Bitmap bmp = BitmapFactory.decodeFile(image_path_string,
					getOptions());
			bmp = BitmapHelper.getImageOrientation(image_path_string, bmp);
			if (bmp != null) {
				bmp = BitmapHelper.scaleCenterCrop(bmp, HEIGHT, WIDTH);
			}

			image_path_string = Funcs.replace(activity, bmp, image_path_string);
			bmp.recycle();
			bmp = null;
			picture = new File(image_path_string);

			// Drawable d = activity.getResources().getDrawable(
			// R.drawable.img_profile_photo);

			// imgPhoto.getLayoutParams().width = d.getIntrinsicWidth();
			// imgPhoto.getLayoutParams().height = d.getIntrinsicHeight();

			// ImageLoader.getInstance().displayImage(
			// "file:///" + image_path_string, imgPhoto);

			// llUploadPhoto.setVisibility(View.GONE);

			return picture;
		} catch (Exception e) {
			if (activity != null)
				Funcs.showShortToast("Unable to get image. Please try again..",
						activity);
			return null;
		}

	}

	public static File retrieveAndDisplayPictureOfDrawableSize(int requestCode,
			int resultCode, Intent data, Activity activity, Drawable d,
			ImageView imgPhoto) {

		switch (requestCode) {

		case GALLERY_REQUEST:
			try {
				Uri selectedImageUri = data.getData();
				image_path_string = Funcs.getPath(selectedImageUri, activity);
				// Bitmap bmp = BitmapFactory.decodeFile(image_path_string,
				// getOptions());
				// bmp = BitmapHelper.getImageOrientation(image_path_string,
				// bmp);
				//
				// image_path_string = Funcs.replace(activity, bmp,
				// image_path_string) ;
				// bmp.recycle();
				// bmp = null;
				picture = new File(image_path_string);

				imgPhoto.getLayoutParams().width = d.getIntrinsicWidth();
				imgPhoto.getLayoutParams().height = d.getIntrinsicHeight();

				ImageLoader.getInstance().displayImage(
						"file:///" + image_path_string, imgPhoto);

				return picture;

			} catch (Exception e) {
				if (activity != null)
					Funcs.showShortToast(
							"Unable to get image. Please try again..", activity);
				return null;
			}

		case CAMERA_REQUEST:
			try {
				File pictureFile = null;
				image_path_string = uriImage.getPath();
				Bitmap bmp = BitmapFactory.decodeFile(image_path_string,
						getOptions());
				bmp = BitmapHelper.getImageOrientation(image_path_string, bmp);
				if (bmp != null) {
					bmp = BitmapHelper.scaleCenterCrop(bmp, HEIGHT, WIDTH);
				}
				image_path_string = Funcs.replace(activity, bmp,
						image_path_string);
				bmp.recycle();
				bmp = null;
				pictureFile = new File(image_path_string);

				// d = activity.getResources().getDrawable(
				// R.drawable.img_btn_signup_addphoto);

				imgPhoto.getLayoutParams().width = d.getIntrinsicWidth();
				imgPhoto.getLayoutParams().height = d.getIntrinsicHeight();

				ImageLoader.getInstance().displayImage(
						"file:///" + image_path_string, imgPhoto);
				return pictureFile;
			} catch (Exception e) {
				e.printStackTrace();
				Funcs.showShortToast("Unable to get image. Please try again..",
						activity);
				return null;
			}

		default:
			return null;

		}

	}

	public static File retrieveAndDisplayPicture(int requestCode,
			int resultCode, Intent data, Activity activity, ImageView imgPhoto) {

		switch (requestCode) {

		case GALLERY_REQUEST:
			try {
				Uri selectedImageUri = data.getData();
				image_path_string = Funcs.getPath(selectedImageUri, activity);
				// Bitmap bmp = BitmapFactory.decodeFile(image_path_string,
				// getOptions());
				// bmp = BitmapHelper.getImageOrientation(image_path_string,
				// bmp);
				//
				// image_path_string = Funcs.replace(activity, bmp,
				// image_path_string) ;
				// bmp.recycle();
				// bmp = null;
				picture = new File(image_path_string);

				ImageLoader.getInstance().displayImage(
						"file:///" + image_path_string, imgPhoto);

				return picture;

			} catch (Exception e) {
				if (activity != null)
					Funcs.showShortToast(
							"Unable to get image. Please try again..", activity);
				return null;
			}

		case CAMERA_REQUEST:
			try {
				File pictureFile = null;
				image_path_string = uriImage.getPath();
				Bitmap bmp = BitmapFactory.decodeFile(image_path_string,
						getOptions());
				bmp = BitmapHelper.getImageOrientation(image_path_string, bmp);
				if (bmp != null) {
					bmp = BitmapHelper.scaleCenterCrop(bmp, HEIGHT, WIDTH);
				}
				image_path_string = Funcs.replace(activity, bmp,
						image_path_string);
				bmp.recycle();
				bmp = null;
				pictureFile = new File(image_path_string);

				// d = activity.getResources().getDrawable(
				// R.drawable.img_btn_signup_addphoto);

				ImageLoader.getInstance().displayImage(
						"file:///" + image_path_string, imgPhoto);
				return pictureFile;
			} catch (Exception e) {
				e.printStackTrace();
				Funcs.showShortToast("Unable to get image. Please try again..",
						activity);
				return null;
			}

		default:
			return null;

		}

	}
	
	public static File retrieveVideo(int requestCode, int resultCode,
			Intent data, Activity activity) {

		try {
			Uri selectedImageUri = data.getData();
			image_path_string = Funcs.getPathForVideo(selectedImageUri, activity);
			
			video = new File(image_path_string);

			return video;
		} catch (Exception e) {
			if (activity != null)
				Funcs.showShortToast("Unable to get video. Please try again..",
						activity);
			return null;
		}

	}

	private static BitmapFactory.Options getOptions() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		return options;
	}

	public static void uploadPhotoDialog(final MainActivity activity) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(activity);
		myAlertDialog.setTitle("Upload Photo");
		myAlertDialog.setMessage("How do you want to set your photo?");

		myAlertDialog.setPositiveButton("Gallery",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						activity.chooseImage();
					}
				});

		myAlertDialog.setNegativeButton("Camera",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						activity.takePicture();
					}
				});
		myAlertDialog.show();
	}

	public static void uploadPhotoDialog(final Fragment fragment,
			Activity activity) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(activity);
		myAlertDialog.setTitle("Upload Photo");
		myAlertDialog.setMessage("How do you want to set your photo?");

		myAlertDialog.setPositiveButton("Gallery",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						uploadFromGallery(fragment);
					}
				});

		myAlertDialog.setNegativeButton("Camera",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						uploadFromCamera(fragment);
					}
				});
		myAlertDialog.show();
	}

	public static void uploadPhotoAndVideoDialog(final Fragment fragment,
			Activity activity) {

		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setTitle("Upload Media");
		alert.setMessage("Choose Media Type");

		alert.setPositiveButton("Photo",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						uploadFromGallery(fragment);
					}
				});

		alert.setNegativeButton("Video",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						uploadVideo(fragment);
					}
				});

		alert.show();

	}

	public static File retrieveAndDisplayPicture(int requestCode,
												 int resultCode, Intent data, Activity activity/*, ImageView imgPhoto*/) {

		switch (requestCode) {

			case GALLERY_REQUEST:
				try {
					Uri selectedImageUri = data.getData();
					image_path_string = Funcs.getPath(selectedImageUri, activity);
					// Bitmap bmp = BitmapFactory.decodeFile(image_path_string,
					// getOptions());
					// bmp = BitmapHelper.getImageOrientation(image_path_string,
					// bmp);
					//
					// image_path_string = Funcs.replace(activity, bmp,
					// image_path_string) ;
					// bmp.recycle();
					// bmp = null;
					picture = new File(image_path_string);

//                    ImageLoader.getInstance().displayImage(
//                            "file:///" + image_path_string, imgPhoto);

					return picture;

				} catch (Exception e) {
					if (activity != null)
						Funcs.showShortToast(
								"Unable to get image. Please try again..", activity);
					return null;
				}

			case CAMERA_REQUEST:
				try {
					File pictureFile = null;
					image_path_string = uriImage.getPath();
					Bitmap bmp = BitmapFactory.decodeFile(image_path_string,
							getOptions());

					bmp = BitmapHelper.getImageOrientation(image_path_string, bmp);
					if (bmp != null) {
						bmp = BitmapHelper.scaleCenterCrop(bmp, HEIGHT, WIDTH);
					}
					image_path_string = Funcs.replace(activity, bmp,
							image_path_string);
					bmp.recycle();
					bmp = null;
					pictureFile = new File(image_path_string);

//                    ImageLoader.getInstance().displayImage(
//                            "file:///" + image_path_string, imgPhoto);
					return pictureFile;
				} catch (Exception e) {
					e.printStackTrace();
					Funcs.showShortToast("Unable to get image. Please try again..",
							activity);
					return null;
				}

			default:
				return null;

		}

	}
}
