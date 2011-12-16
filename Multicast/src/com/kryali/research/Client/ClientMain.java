package com.kryali.research.Client;

import com.kryali.research.MessageType;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ClientMain extends Thread {
	
	private Handler viewHandler = null;
	private ClientTracker clientTrackerThread = null;
	private Context context = null;

	public ClientMain(Handler viewHandler, Context viewContext) {
		this.viewHandler  = viewHandler;
		this.context = viewContext;
        clientTrackerThread = new ClientTracker(viewContext);
		this.start();
	}
	
	public void shutdown() {
		clientTrackerThread.shutdown();
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

}
