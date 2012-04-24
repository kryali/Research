package com.kryali.research.Client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.kryali.research.MessageType;
import com.kryali.research.Phone;
import com.kryali.research.R;

public class ClientActivity extends Activity {

	private static final String TAG = "ClientActivity";
	ClientMain clientMain;
	ClientTracker clientTracker;
	Phone hostPhone;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_view);
		
		Log.i("","WHERE IS YOUR GOD");
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			String phoneData = extras.getString("phone");
			hostPhone = new Phone(phoneData);
			initView(hostPhone);;
			clientMain = new ClientMain(viewHandler, getBaseContext());
			clientTracker = new ClientTracker(getBaseContext());
			clientTracker.register(hostPhone.getId());
			Log.i(TAG, "Hopefully it registerd?");
		}
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (clientMain != null)
			clientMain.shutdown();
		if( clientTracker != null )
			clientTracker.shutdown();
	}

	private void initView(Phone phone) {

		TextView tv = (TextView) findViewById(R.id.hostname);
		tv.setText(phone.getLocalIP());

		tv = (TextView) findViewById(R.id.model);
		tv.setText(phone.getModel());
	}
}