package com.kryali.research.Client;

import android.content.Context;
import android.util.Log;

import com.kryali.research.Hardware;
import com.kryali.research.Network.TransferManager;
import com.kryali.research.Network.URLS;

public class ClientTracker extends Thread {

	private Context activityContext;
	private boolean keepAlive = true;
	private String TAG = ClientTracker.class.getSimpleName();

	/*
	 * Constructor
	 */
	public ClientTracker(Context activityContext) {
		this.activityContext = activityContext;
		this.start();
	}

	public void shutdown() {
		keepAlive = false;
	}
	
	public void register(String id) {
		String phone = Hardware.toJSONString(activityContext);
		String url = URLS.registerNewClientURL(id);
		TransferManager.POST(url, phone);
		Log.i("", "Attempted to register client " + url);
	}

	public void run() {
		while ( keepAlive ) {
		}
	}
}
