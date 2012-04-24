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

	PreviewCallback mPreviewCallback = new PreviewCallback() {
		private int COMPRESSION_RATE = 20;

		public void onPreviewFrame(byte[] data, Camera camera) {
			try {
				// YuvImage yuvi = new YuvImage(data, ImageFormat.NV21, width,
				// height, null);
				// Rect rect = new Rect(0,0,width,height);
				// BOutputStream bos = new BOutputStream();
				// yuvi.compressToJpeg(rect, COMPRESSION_RATE , bos);
				// bos.send_udp(client.getInetAddress().getHostName(),
				// VideoDecodeThread.PORT);
				// Log.i(TAG, "sent frame");
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
			;
		}
	};

	public SurfaceHolderCallback(SurfaceHolder holder) {
		this.holder = holder;
	}

	public SurfaceHolderCallback(SurfaceHolder holder, PreviewCallback callback) {
		this.holder = holder;
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
			mCamera.setPreviewCallback(mPreviewCallback);
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
