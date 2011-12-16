package com.kryali.research;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class ClientActivity extends Activity {
    private ClientMain clientMainThread = null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client);
        
        clientMainThread  = new ClientMain(viewHandler, getBaseContext());
    }

    private final Handler viewHandler = new Handler() {
    	
    };
}
