package com.app.court.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenHelper {

	public static int getDensityAsInteger(Context context) {

		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);

		return metrics.densityDpi;
	}

	public static int getSizeAsInteger(Context context) {

		int screenSize = context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;

		return screenSize;

	}

	public static String getDensity(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			return "Low";
		case DisplayMetrics.DENSITY_MEDIUM:
			return "Medium";
		case DisplayMetrics.DENSITY_HIGH:
			return "High";
		case DisplayMetrics.DENSITY_TV:
			return "TV";
		case DisplayMetrics.DENSITY_XHIGH:
			return "XHigh";
		case DisplayMetrics.DENSITY_XXHIGH:
			return "XXHigh";
		default:
			return "Unknown";

		}
	}

	public static String getSize(Context context) {
		int screenLayout = context.getResources().getConfiguration().screenLayout;
		screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

		switch (screenLayout) {
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			return "Small";
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			return "Normal";
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			return "Large";
		case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
			return "XLarge";
		default:
			return "Unknown";
		}
	}

}
