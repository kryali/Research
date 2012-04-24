package com.kryali.research.Network;

public class URLS {
	
	private final static String host = "192.168.1.106";
	private final static String port = "20000";
	private final static String hosts = "hosts.json";
	private final static String newHost = "hosts/new";
	private final static String remHost = "hosts/remove";
	private final static String getClients = "hosts/";
	
	public static String getBaseURL() {
		return "http://" + host + ":" + port + "/" ;
	}
	public static String getAllHostsURL() {
		return getBaseURL() + hosts;
	}
	
	public static String getNewHostURL() {
		return getBaseURL() + newHost;
	}
	public static String remHost() {
		return getBaseURL() + remHost;
	}
	public static String getClientsURL(String id) {
		return getBaseURL() + getClients + id + ".json";
	}
	public static String registerNewClientURL(String id) {
		return getBaseURL() + "hosts/" + id + "/client/new" ;
	}
}
