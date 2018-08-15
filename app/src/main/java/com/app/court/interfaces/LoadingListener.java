package com.app.court.interfaces;

public interface LoadingListener {

	public void onLoadingStarted();

	public void onLoadingFinished();

	/**
	 * @param percentLoaded
	 *            should be between 1 and 100
	 */
	public void onProgressUpdated(int percentLoaded);
}
