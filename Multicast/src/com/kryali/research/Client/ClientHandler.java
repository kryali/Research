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
	private ObjectInputStream inputStream;
	private VideoClientBuffer buffer;

	public ClientHandler(Socket socket, Handler viewHandler, VideoClientBuffer buffer) {
		this.socket = socket;
		this.viewHandler = viewHandler;
		this.buffer = buffer;
		keepAlive = true;
		this.start();
	}

	public void shutdown() {
		keepAlive = false;
	}

	@Override
	public void run() {
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			while (keepAlive) {
				int packetType = inputStream.readInt();
				handlePacket(packetType);
			}
			inputStream.close();
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

	private void acceptFrame() {
		try {
			int frameId = inputStream.readInt();
			int frameLength = inputStream.readInt();
			Log.i(TAG, "Received a frame id " + frameId + "-" + frameLength);
			byte[] data = new byte[frameLength];
			inputStream.readFully(data,0,frameLength);
			buffer.addFrame(frameId, data);
//			Log.i(TAG, "Frame: ["+frameId+"]"+" " + data.length);
			/* Notify the view that a new frame has arrived, might not be necessary 
			Message startMessage = new Message();
			Bundle extras = new Bundle();
			extras.putBoolean(MessageType.FRAME, true);
			startMessage.setData(extras);
			this.viewHandler.sendMessage(startMessage);
			*/

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handlePacket(int packetType) {
		Log.i(TAG, "recieved packet: " + packetType );
		switch (packetType) {
		case Packet.SIGNAL_START:
			signalStart();
			break;
		case Packet.SIGNAL_END:
			signalEnd();
			break;
		case Packet.FRAME:
			acceptFrame();
			break;
		}
	}

}
