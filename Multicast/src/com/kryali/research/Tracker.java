package com.kryali.research;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.util.Log;

/*
 * This class sends messages back and forth 
 * between the android device and the cloud
 */

public class Tracker extends Thread {

	private Context activityContext;
	private boolean keepAlive = false;
	private String TAG = Tracker.class.getSimpleName();

	/*
	 * Constructor
	 */
	public Tracker(Context activityContext) {
		this.activityContext = activityContext;
		keepAlive = true;
		this.start();
	}

	public void shutdown() {
		keepAlive = false;
	}

	public void GET(String hostname) {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL( "http://www.kryali.com:20000/" );
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		} 
		catch (Exception ignored) {
			
		}
		finally {
			if(urlConnection != null)
				urlConnection.disconnect();
		}
	}

	public void POST(String hostname) {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL( hostname );
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setChunkedStreamingMode(0);
			urlConnection.setRequestProperty("Content-Type", "application/json");
			//OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
		} 
		catch (Exception ignored) {
			
		}
		finally {
			if(urlConnection != null)
				urlConnection.disconnect();
		}
	}

	public void run() {
		String id = Hardware.id( activityContext );
		Log.i(TAG, "ID: " + id);
		Hardware.LogState();
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
