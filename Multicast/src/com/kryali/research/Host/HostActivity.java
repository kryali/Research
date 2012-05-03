package com.kryali.research.Host;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.kryali.research.MessageType;
import com.kryali.research.R;
import com.kryali.research.SurfaceHolderCallback;
import com.kryali.research.VideoQueueBuffer;

public class HostActivity extends Activity {
	private HostMain hostMain = null;
	private HostTracker tracker = null;
	private SurfaceView mSurfaceView;
	VideoQueueBuffer buffer;
	public static final int FPS = 30;
	public static final int SEC = 5;
	public static final int BUFFER_SIZE = FPS * SEC;
	public static final int WIDTH = 352;
	public static final int HEIGHT = 288;
	private static boolean streaming = false;
	private SeekBar compressionBarView;
	private TextView compressionLabel;
	private int ratio = 20;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host);

		Context context = getBaseContext();
		// Start the main thread
		buffer = new VideoQueueBuffer();
		hostMain = new HostMain(viewHandler, context, buffer);
		tracker = new HostTracker(context);
		initHolder();
		initButton();
		initSeekBar();
	}

	public int getCompressionRatio() {
		return ratio;
	}

	public void setCompressionRatio(int progress) {
		ratio = progress;
		compressionLabel.setText(progress + " %");
	}

	public void initSeekBar() {
		compressionBarView = (SeekBar) findViewById(R.id.compressionBar);
		compressionBarView
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						setCompressionRatio(progress);
					}
				});
		compressionLabel = (TextView) findViewById(R.id.compressionLabel);
	}

	PreviewCallback mPreviewCallback = new PreviewCallback() {
		private long lastTime = -1;
		private String TAG = "HostActivity";

		public void onPreviewFrame(byte[] data, Camera camera) {
			if (!streaming)
				return;
			Calendar cal = Calendar.getInstance();
			long currentTime = cal.getTimeInMillis();
			if (lastTime == -1)
				lastTime = currentTime;
			else {
				// Only fire 30 times a second
				if ((float) (currentTime - lastTime) < (1000f / (float) FPS))
					return;
				else
					lastTime = currentTime;
			}
			
			try {
				YuvImage yuvi = new YuvImage(data, ImageFormat.NV21, WIDTH,
						HEIGHT, null);
				Rect rect = new Rect(0, 0, WIDTH, HEIGHT);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				yuvi.compressToJpeg(rect, getCompressionRatio(), os);
				byte[] jpegData = os.toByteArray();
				// Log.i("", "JPEGSIZE=" + jpegData.length);
				buffer.addFrame(jpegData);
			} catch (Exception e) {
				Log.e("", e.toString());
			}
		}
	};
	private SurfaceHolder holder;

	private void initButton() {
		Button start = (Button) findViewById(R.id.startStream);
		Button stop = (Button) findViewById(R.id.stopStream);

		start.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				streaming = true;
				hostMain.startStreaming();
			}
		});

		stop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				streaming = false;
				hostMain.stopStreaming();
			}
		});
	}

	private void initHolder() {
		mSurfaceView = (SurfaceView) findViewById(R.id.video_surface_view);
		holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		SurfaceHolderCallback callback = new SurfaceHolderCallback(holder,
				mPreviewCallback);
		holder.addCallback(callback);
	}

	@Override
	public void onDestroy() {
		if (hostMain != null)
			hostMain.shutdown();
		if (tracker != null)
			tracker.shutdown();

		super.onDestroy();
	}

	private final Handler viewHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			if (data.getBoolean(MessageType.MainStart)) {
				Toast.makeText(getApplicationContext(), "Started...",
						Toast.LENGTH_SHORT).show();
			} else if (data.getBoolean(MessageType.MainEnd)) {
				Toast.makeText(getApplicationContext(), "Finished...",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
}
