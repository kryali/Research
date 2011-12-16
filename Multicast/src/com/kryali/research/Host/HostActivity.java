package com.kryali.research.Host;

import com.kryali.research.MessageType;
import com.kryali.research.R;
import com.kryali.research.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class HostActivity extends Activity {
    private HostMain hostMain = null;
	private HostTracker tracker = null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host);
        
        Context context = getBaseContext();
        // Start the main thread
        hostMain  = new HostMain( viewHandler, context );
        tracker = new HostTracker( context );
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
