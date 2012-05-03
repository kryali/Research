package com.kryali.research.Client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kryali.research.MessageType;
import com.kryali.research.Phone;
import com.kryali.research.R;
import com.kryali.research.Host.HostActivity;

public class ClientActivity extends Activity {

	private static final String TAG = "ClientActivity";
	ClientMain clientMain;
	ClientTracker clientTracker;
	Phone hostPhone;
	private VideoClientBuffer buffer;
	private ImageView iv;
	private TextView fpsCountView;
	private long lastUpdateTime = 0;
	private int framesDisplayedCount = 0;
	private long frameRefreshSpeed = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_view);

		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			String phoneData = extras.getString("phone");
			hostPhone = new Phone(phoneData);
			buffer = new VideoClientBuffer();
			initView(hostPhone);
			clientMain = new ClientMain(viewHandler, getBaseContext(), buffer);
			clientTracker = new ClientTracker(getBaseContext());
			clientTracker.register(hostPhone.getId());
			iv = (ImageView) findViewById(R.id.image);
			fpsCountView = (TextView) findViewById(R.id.fps);
			frameRefreshSpeed = 10; // (long) (1000f / (float) HostActivity.FPS)
		}
		Log.i(TAG, "FOR THE LOVE OF GOD MAN");
	}

	private final Handler viewHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			if (data.getBoolean(MessageType.MainStart)) {
				Toast.makeText(getApplicationContext(), "Started",
						Toast.LENGTH_SHORT).show();
				viewHandler.postDelayed(updateFrameTask, frameRefreshSpeed);
				viewHandler.postDelayed(updateFPSTask, 1000);
			} else if (data.getBoolean(MessageType.MainEnd)) {
				Toast.makeText(getApplicationContext(), "Finished",
						Toast.LENGTH_SHORT).show();
				viewHandler.removeCallbacks(updateFrameTask);
				viewHandler.removeCallbacks(updateFPSTask);
			}
		}
	};

	private Runnable updateFPSTask = new Runnable() {
		public void run() {
			float fps = framesDisplayedCount;
			String text = fps + " FPS";
			fpsCountView.setText(text);

			// drop frames, since we didn't display 30 frames fast enough
			int dropFrames = 30 - framesDisplayedCount;
			while (dropFrames > 0) {
				buffer.dropFrame();
				dropFrames--;
			}

			framesDisplayedCount = 0;
			viewHandler.postDelayed(this, 1000);
		}
	};

	private Runnable updateFrameTask = new Runnable() {
		public void run() {
			Bitmap bitmap = buffer.getNextFrame();
			if (bitmap != null) {
				iv.setImageBitmap(bitmap);
				framesDisplayedCount++;
			}
			viewHandler.postDelayed(this, frameRefreshSpeed);
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (clientMain != null)
			clientMain.shutdown();
		if (clientTracker != null)
			clientTracker.shutdown();
		viewHandler.removeCallbacks(updateFrameTask);
		viewHandler.removeCallbacks(updateFPSTask);
	}

	private void initView(Phone phone) {

		TextView tv = (TextView) findViewById(R.id.hostname);
		tv.setText(phone.getLocalIP());

		tv = (TextView) findViewById(R.id.model);
		tv.setText(phone.getModel());
	}
}