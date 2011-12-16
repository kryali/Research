package com.kryali.research;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class HostActivity extends Activity {
    private HostMain hostMain = null;
	private Tracker tracker = null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host);
        
        Context context = getBaseContext();
        // Start the main thread
        hostMain  = new HostMain( viewHandler, context );
        tracker = new Tracker( context );
    }
    
    @Override
    public void onDestroy() {
    	hostMain.shutdown();
    	tracker.shutdown();
    	super.onDestroy();
    }
    
    private final Handler viewHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		Bundle data = msg.getData();
    		if(data.getBoolean(MessageType.MainStart)) {
    			Toast.makeText(getApplicationContext(), "Started...", Toast.LENGTH_SHORT).show();
    		}
    		else if(data.getBoolean(MessageType.MainEnd)){
    			Toast.makeText(getApplicationContext(), "Finished...", Toast.LENGTH_SHORT).show();
    		}
    	}
    };
}
