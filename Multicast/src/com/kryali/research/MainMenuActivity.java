package com.kryali.research;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kryali.research.Client.ClientListActivity;
import com.kryali.research.Host.HostActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity {
    private String TAG = "MainMenuActivity";
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        setUpListeners();
    }
       
    private void setUpListeners() {
    	Button host = (Button) findViewById(R.id.host_button);
    	Button client = (Button) findViewById(R.id.client_button);
    	host.setOnClickListener(hostClickListener);
    	client.setOnClickListener(clientClickListener);
    }

    private OnClickListener hostClickListener = new OnClickListener() {

		public void onClick(View arg0) {
			Intent i = new Intent(MainMenuActivity.this, HostActivity.class);
			startActivity(i);
		}
    	
    };
    private OnClickListener clientClickListener = new OnClickListener() {

		public void onClick(View arg0) {
			Intent i = new Intent(MainMenuActivity.this, ClientListActivity.class);
			startActivity(i);
		}
    };
}