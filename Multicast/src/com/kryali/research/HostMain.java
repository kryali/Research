package com.kryali.research;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HostMain extends Thread {
	
	private Handler viewHandler = null;
	private Context viewContext = null;
	private String TAG = HostMain.class.getName();

	public HostMain(Handler viewHandler, Context viewContext) {
		this.viewHandler  = viewHandler;
		this.viewContext = viewContext;
		this.start();
	}
	
	private void signalStart() {
		Message startMessage = new Message();
		Bundle data = new Bundle();
		data.putBoolean(MessageType.MainStart, true);
		startMessage.setData(data);
		this.viewHandler.sendMessage(startMessage);
	}
	
	private void signalEnd() {
		Message startMessage = new Message();
		Bundle data = new Bundle();
		data.putBoolean(MessageType.MainEnd, true);
		startMessage.setData(data);
		this.viewHandler.sendMessage(startMessage);
	}
	
	@Override
	public void run() {
		signalStart();
		signalEnd();
	}

	public void shutdown() {
		// Put thread cleanup here
	}

}
