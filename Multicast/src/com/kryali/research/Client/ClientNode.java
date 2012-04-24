package com.kryali.research.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import android.util.Log;

public class ClientNode {

	private static int count = 0;
	private int id;
	private Socket socket = null;
	private ObjectOutputStream outputStream = null;
	private ObjectInputStream inputStream = null;
	private String TAG = "ClientNode";
	
	public ClientNode(Socket socket) throws StreamCorruptedException, IOException {
		setSocket(socket);
		setInputStream(new ObjectInputStream(socket.getInputStream()));
		setOutputStream(new ObjectOutputStream(socket.getOutputStream()));
		setId(count);
		count++;
	}

	public ClientNode(Socket socket, ObjectOutputStream outputStream,
			ObjectInputStream inputStream) {
		setSocket(socket);
		setOutputStream(outputStream);
		setInputStream(inputStream);
		setId(count);
		count++;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		ClientNode.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}
}
