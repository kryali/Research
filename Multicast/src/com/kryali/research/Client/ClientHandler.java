package com.kryali.research.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kryali.research.MessageType;
import com.kryali.research.Packet;

public class ClientHandler extends Thread {

	String TAG = "ClientHandler";
	Socket socket;
	boolean keepAlive = true;
	Handler viewHandler;

	public ClientHandler(Socket socket, Handler viewHandler) {
		this.socket = socket;
		this.viewHandler = viewHandler;
		keepAlive = true;
		this.start();
	}

	public void shutdown() {
		keepAlive = false;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			while (keepAlive) {
				int packetType = ois.readInt();
				handlePacket(packetType);
			}
			ois.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void signalStart() {
		Log.i(TAG, "HOST SENT SIGNAL START");
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
		this.shutdown();
	}

	private void handlePacket(int packetType) {
		switch (packetType) {
		case Packet.SIGNAL_START:
			signalStart();
			return;
		case Packet.SIGNAL_END:
			signalEnd();
			return;
		}
	}

}
