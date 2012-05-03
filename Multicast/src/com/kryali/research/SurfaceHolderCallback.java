package com.kryali.research;

import java.net.Socket;
import java.util.List;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

public class SurfaceHolderCallback implements Callback {

	protected static final String TAG = "SurfaceHolder";
	public Camera mCamera;
	public SurfaceHolder holder;
	private int width = 352;
	private int height = 288;

	private PreviewCallback callback;

	public SurfaceHolderCallback(SurfaceHolder holder, PreviewCallback callback) {
		this.holder = holder;
		this.callback = callback;
	}

	public void initCamera() {
		mCamera = Camera.open();
		Parameters params = mCamera.getParameters();
		params.setPreviewSize(width, height);

		List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
		for (int i = 0; i < previewSizes.size(); i++) {
			Log.i(TAG, previewSizes.get(i).width + " x "
					+ previewSizes.get(i).height);
		}
		params.setPreviewFormat(ImageFormat.NV21);
		// params.setFlashMode(Parameters.FLASH_MODE_ON);
		mCamera.setParameters(params);

		try {
			mCamera.setPreviewCallback(callback);
			mCamera.setPreviewDisplay(holder);
		} catch (Exception e) {
			Log.e("exception", e.toString());
			return;
		}
		mCamera.startPreview();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		initCamera();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}
}
