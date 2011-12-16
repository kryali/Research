package com.kryali.research;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.util.Log;


// SOURCE: http://stackoverflow.com/questions/3118234/how-to-get-memory-usage-and-cpu-usage-in-android
public class Hardware {
	private static final String TAG = "Hardware";
	
	public static float getCPUUtil() {
	    try {
	        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
	        String load = reader.readLine();

	        String[] toks = load.split(" ");

	        long idle1 = Long.parseLong(toks[5]);
	        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

	        try {
	            Thread.sleep(360);
	        } catch (Exception e) {}

	        reader.seek(0);
	        load = reader.readLine();
	        reader.close();

	        toks = load.split(" ");

	        long idle2 = Long.parseLong(toks[5]);
	        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
	            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

	        return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }

	    return 0;
	} 
	

    public static int getBattery() {
		String batteryPath = "/sys/class/power_supply/battery/capacity";
		try {
			BufferedReader in = new BufferedReader(new FileReader(batteryPath));
			String line = in.readLine();
			int batteryLevel = Integer.parseInt(line);
			return batteryLevel;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			return 0;
		}
    }
    
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    public static void LogState() {
    	String board = Build.BOARD;
    	String manufacturer = Build.MANUFACTURER;
    	String display  = Build.DISPLAY;
    	String model = Build.MODEL;
    	String user = Build.USER;
    	String host = Build.HOST;
    	String cpuAbi = Build.CPU_ABI;
    	String product = Build.PRODUCT;
    	Log.i(TAG, "Board: " + board);
    	Log.i(TAG, "Manufacturer: " + manufacturer);
    	Log.i(TAG, "Display: " + display);
    	Log.i(TAG, "Model: " + model);
    	Log.i(TAG, "user: " + user);
    	Log.i(TAG, "host: " + host);
    	Log.i(TAG, "cpuAbi: " + cpuAbi);
    	Log.i(TAG, "product: " + product);
    	/*Build.BRAND.length()%10 +
        Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
        Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
        Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
        Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
        Build.TAGS.length()%10 + Build.TYPE.length()%10 +
        Build.USER.length()%10 ; //13 digits*/
    }
    
}
