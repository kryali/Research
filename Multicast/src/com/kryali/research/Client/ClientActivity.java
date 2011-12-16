package com.kryali.research.Client;

import com.kryali.research.R;
import com.kryali.research.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class ClientActivity extends Activity {
    private ClientMain clientMainThread = null;
	private ClientTracker clientTrackerThread = null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client);
        
        clientMainThread  = new ClientMain(viewHandler, getBaseContext());
    }
    
    public void onDestroy() {
    	clientMainThread.shutdown();
    	super.onDestroy();
    }

    private final Handler viewHandler = new Handler() {
    	
    };
}
