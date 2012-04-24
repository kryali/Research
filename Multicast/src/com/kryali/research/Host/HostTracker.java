package com.kryali.research.Host;

import org.json.JSONArray;
import org.json.JSONException;

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
	private String id;

	/*
	 * Constructor
	 */
	public HostTracker(Context context) {
		this.context = context;
		keepAlive = true;
		this.start();
	}
	
	private String getDeviceId() {
		if( id == null) {
			id = Hardware.id(context);
		}
		return id;
	}

	public void shutdown() {
		keepAlive = false;
	}
	
	public void register() {
		Log.i(TAG, "Registering with tracker");
		
		String hardwareJSON = Hardware.toJSONString(context);
		TransferManager.POST( URLS.getNewHostURL(), hardwareJSON );
	}

	public void deregister() {
		Log.i(TAG, "DEREGISTER with tracker");
		TransferManager.deregister(getDeviceId());
	}
	
	public JSONArray getClients() {
		String jsonData = TransferManager.GET( URLS.getClientsURL(getDeviceId()) );
		JSONArray data = null;
		try {
			data = new JSONArray(jsonData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
	public void run() {
		register();
		while ( keepAlive ) {
			// TODO: Talk to the server
//			Log.i(TAG, "Tracker alive");
			try {
				Thread.sleep(2500);
			} catch (Exception ignored) {}
		}
		deregister();
	}
}
