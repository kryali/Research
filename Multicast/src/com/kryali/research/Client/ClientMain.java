package com.kryali.research.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kryali.research.MessageType;
import com.kryali.research.Packet;

public class ClientMain extends Thread {

	public static final int PORT = 50001;
	private static final String TAG = "ClientMain";
	private Handler viewHandler = null;
	private ClientTracker clientTrackerThread = null;
	private Context context = null;
	private boolean keepAlive = true;
	private ServerSocketChannel servSock;
	private LinkedList<ClientHandler> nodeHandlers;

	public ClientMain(Handler viewHandler, Context viewContext) {
		this.viewHandler = viewHandler;
		this.context = viewContext;
		// clientTrackerThread = new ClientTracker(viewContext);
		keepAlive = true;
		nodeHandlers = new LinkedList<ClientHandler>();
		this.start();
	}

	public void shutdown() {
		if (clientTrackerThread != null)
			clientTrackerThread.shutdown();
		for (ClientHandler handler : nodeHandlers) {
			handler.shutdown();
		}
		keepAlive = false;
	}

	@Override
	public void run() {
		SocketChannel socketChannel;
		try {
			servSock = ServerSocketChannel.open();
			servSock.socket().bind(new InetSocketAddress(PORT));
			servSock.configureBlocking(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (keepAlive) {
			try {
				socketChannel = servSock.accept();
				if (socketChannel != null) {
					Log.i(TAG, "Got a connection!");
					ClientHandler handler = new ClientHandler(
							socketChannel.socket(), viewHandler);
					nodeHandlers.add(handler);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			servSock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
