package com.tomgibara.android.camera;

import android.graphics.Bitmap;
import android.view.SurfaceHolder;

/**
 * Provides a simple abstraction for obtaining preview captures from a camera
 * on the Android platform. This interface intended to be used temporarily while
 * the Google Android SDK fails to support camera capture from desktop devices
 * (webcams etc).
 * 
 * @author Tom Gibara
 */

public interface CameraSource {

	static final String LOG_TAG = "camera";
	
	/**
	 * Open the camera source for subsequent use via calls to capture().
	 * 
	 * @return true if the camera source was successfully opened.
	 */
	
	boolean open();
	
	/**
	 * Close the camera source. Calling close on a closed CameraSource is
	 * permitted but has no effect. The camera source may be reopened after
	 * being closed.
	 */
	
	void close();
	
	/**
	 * Attempts to render the current camera.
	 * The capture will be rendered into the rectangle (0,0,width,height).
	 * Outstanding transformations on the canvas may alter this.
	 * 
	 * @param canvas the canvas to which the captured pixel data will be written
	 * @return true iff a frame was successfully written to the canvas
	 */
	
	void capture(CameraCallback callback);

	public interface CameraCallback {
		public void onPictureTaken(Bitmap bitmap);
	}
	
	public void startPreview(SurfaceHolder holder);
	public void stopPreview();
}
