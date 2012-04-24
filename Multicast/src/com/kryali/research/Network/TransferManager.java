package com.kryali.research.Network;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/*
 * Used for doing get and post requests
 */
public class TransferManager {

	private final static String TAG = TransferManager.class.getSimpleName();
	
	public final static int PORT = 42100;
	
	public static String getString(InputStream in) throws IOException {
		    BufferedInputStream bis = new BufferedInputStream(in);
		    ByteArrayOutputStream buf = new ByteArrayOutputStream();
		    int result = bis.read();
		    while(result != -1) {
		      byte b = (byte)result;
		      buf.write(b);
		      result = bis.read();
		    }        
		    return buf.toString();
	}
	
	public static String GET(String hostname) {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL( hostname );
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			String response = getString(in);
			return response;
			
		} 
		catch (Exception ignored) {
			Log.e(TAG, ignored.toString());
		}
		finally {
			if(urlConnection != null)
				urlConnection.disconnect();
		}
		return null;
	}

	public static void POST(String hostname, String payload) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL( hostname );
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setChunkedStreamingMode(0);
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream out = conn.getOutputStream();
			out.write( payload.getBytes() );
			out.flush();
			out.close();
		} 
		catch (Exception ignored) {
			
		}
		finally {
			if(conn != null)
				conn.disconnect();
		}
	}
	
	public static void deregister( String id ) {
		JSONObject data = new JSONObject();
		try {
			data.put("id", id);
			data.put("action", "deregister");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String payload = data.toString();
		TransferManager.POST( URLS.remHost(), payload);
	}
}
