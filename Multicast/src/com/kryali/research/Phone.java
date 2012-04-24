package com.kryali.research;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Phone {

	private int port;
	private String id;
	private String hostname;
	private String model;
	private String dhcp;

	public String getDhcp() {
		return dhcp;
	}

	public void setDhcp(String dhcp) {
		this.dhcp = dhcp;
	}
	
	public Phone(JSONObject data) {
		init(data);
	}

	private void init(JSONObject data) {
		try {
			setPort(data.getInt("port"));
			setModel(data.getString("model"));
			setHostname(data.getString("hostname"));
			setDhcp(data.getString("dhcp data"));
			setId(data.getString("id"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Phone(String phoneData) {
		JSONObject data;
		try {
			data = new JSONObject(phoneData);
			init(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return getModel();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLocalIP() {
		String data = getDhcp();
		String pattern = "ipaddr\\s[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(data);
		if( m.find() ) {
			String ip = m.group(0);
			return ip.replace("ipaddr ","");
		}
		return data;
	}
}
