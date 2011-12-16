package com.kryali.research.Client;

import android.content.Context;
import android.util.Log;

import com.kryali.research.Network.TransferManager;
import com.kryali.research.Network.URLS;

/*
 * This class sends messages back and forth 
 * between the android device and the cloud
 */

public class ClientTracker extends Thread {

	private Context activityContext;
	private boolean keepAlive = false;
	private String TAG = ClientTracker.class.getSimpleName();

	/*
	 * Constructor
	 */
	public ClientTracker(Context activityContext) {
		this.activityContext = activityContext;
		keepAlive = true;
		this.start();
	}

	public void shutdown() {
		keepAlive = false;
	}
	
	public void getHosts() {
		Log.i(TAG, "Requesting available hosts");
		String response = TransferManager.GET( URLS.getAllHostsURL() );
		Log.i(TAG, "response: " + response);
	}


	public void run() {
		getHosts();
		while ( keepAlive ) {
			// TODO: Talk to the server
			Log.i(TAG, "Client Tracker alive");
			try {
				Thread.sleep(2500);
			} catch (Exception ignored) {
			}
		}
	}
}
