package com.kryali.research.Host;

import android.content.Context;
import android.util.Log;

import com.kryali.research.Hardware;
import com.kryali.research.Network.TransferManager;
import com.kryali.research.Network.URLS;

/*
 * This class sends messages back and forth 
 * between the android device and the cloud
 */

public class HostTracker extends Thread {

	private Context context;
	private boolean keepAlive = false;
	private String TAG = HostTracker.class.getSimpleName();

	/*
	 * Constructor
	 */
	public HostTracker(Context context) {
		this.context = context;
		keepAlive = true;
		this.start();
	}

	public void shutdown() {
		keepAlive = false;
	}
	
	public void register() {
		Log.i(TAG, "Registering with tracker");
		
		String hardwareJSON = Hardware.toJSONString(context);
		TransferManager.POST( URLS.getNewHostURL(), hardwareJSON );
	}


	public void run() {
		register();
		while ( keepAlive ) {
			// TODO: Talk to the server
			Log.i(TAG, "Tracker alive");
			try {
				Thread.sleep(2500);
			} catch (Exception ignored) {
			}
		}
	}
}
