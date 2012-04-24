package com.kryali.research.Host;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.kryali.research.MessageType;
import com.kryali.research.R;
import com.kryali.research.SurfaceHolderCallback;

public class HostActivity extends Activity {
	private HostMain hostMain = null;
	private HostTracker tracker = null;
	private SurfaceView mSurfaceView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host);

		Context context = getBaseContext();
		// Start the main thread
		hostMain = new HostMain( viewHandler, context );
		tracker = new HostTracker( context );
		initHolder();
		initButton();
	}
	
	private void initButton() {
		Button start = (Button) findViewById(R.id.startStream);
		Button stop = (Button) findViewById(R.id.stopStream);
		
		start.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				hostMain.startStreaming();
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				hostMain.stopStreaming();
			}
		});
	}

	private void initHolder() {
		mSurfaceView = (SurfaceView) findViewById(R.id.video_surface_view);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		SurfaceHolderCallback callback = new SurfaceHolderCallback(holder);
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
