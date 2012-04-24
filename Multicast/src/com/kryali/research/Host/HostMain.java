package com.kryali.research.Host;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.kryali.research.Hardware;
import com.kryali.research.Packet;
import com.kryali.research.Phone;
import com.kryali.research.Client.ClientMain;
import com.kryali.research.Client.ClientNode;
import com.kryali.research.Network.TransferManager;
import com.kryali.research.Network.URLS;

public class HostMain extends Thread {

	private Handler viewHandler = null;
	private Context viewContext = null;
	private String TAG = HostMain.class.getName();
	private boolean keepAlive = true;
	private LinkedList<Phone> clientNodes;
	private LinkedList<ClientNode> clients;
	private boolean streaming = false;

	public HostMain(Handler viewHandler, Context viewContext) {
		this.viewHandler = viewHandler;
		this.viewContext = viewContext;
		clientNodes = new LinkedList<Phone>();
		clients = new LinkedList<ClientNode>();
		this.start();
	}

	public void shutdown() {
		for (ClientNode client: clients) {
			try {
				ObjectOutputStream stream = client.getOutputStream();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		keepAlive = false;
	}

	public void openStreams() {
		for (Phone client : clientNodes) {
			String ip = client.getLocalIP();
			Socket s;
			try {
				Log.i(TAG, "Connecting to client at " + ip + ":" + ClientMain.PORT);
				s = new Socket(ip, ClientMain.PORT);
				ObjectOutputStream oos = new ObjectOutputStream( s.getOutputStream() );
//				ObjectInputStream ois = new ObjectInputStream( s.getInputStream() );
				ClientNode node = new ClientNode(s, oos, null);
				clients.add(node);
			} catch (UnknownHostException e) {
				Log.e("", "Unknown host");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	
	private void getClients() {
		String id = Hardware.id(viewContext);
		String hostData = TransferManager.GET(URLS.getClientsURL(id));
		Log.i(TAG, hostData);
		try {
			JSONObject host = new JSONObject(hostData);
			host = host.getJSONObject("host");
			JSONArray nodes = host.getJSONArray("nodes");
			for(int i = 0; i < nodes.length(); i++) {
				JSONObject node = (JSONObject) nodes.get(i);
				Phone p = new Phone(node);
				clientNodes.add(p);
				Log.i(TAG, p.getModel());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void startStreaming() {
		if(streaming) return;
		Log.i(TAG,"START STREAM, fetching clients");
		getClients();
		Log.i(TAG,"Clients fetched");
		openStreams();
		Log.i(TAG, "Opening streams");
		for (ClientNode client: clients) {
			try {
				Log.i(TAG, "Delivering start signal to " + client.getId());
				ObjectOutputStream stream = client.getOutputStream();
				stream.writeInt(Packet.SIGNAL_START);
				stream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.i(TAG, "Delivered all start signals");
		streaming  = true;
	}

	public void stopStreaming() {
		Log.i(TAG,"STOP STREAM");
		for (ClientNode client: clients) {
			try {
				ObjectOutputStream stream = client.getOutputStream();
				stream.writeInt(Packet.SIGNAL_END);
				stream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.i(TAG,"Delivered end signal");
		streaming = false;
	}

	@Override
	public void run() {
		while (keepAlive) {
			if(streaming) {
				// TODO: something
			}
		}
		// signalStart();
		// signalEnd();
	}

}
