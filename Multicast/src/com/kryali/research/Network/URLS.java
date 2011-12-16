package com.kryali.research.Network;

public class URLS {
	
	private final static String host = "50.57.148.240";
	private final static String port = "20000";
	private final static String hosts = "hosts.json";
	private final static String newHost = "hosts/new";
	
	public static String getBaseURL() {
		return "http://" + host + ":" + port + "/" ;
	}
	public static String getAllHostsURL() {
		return getBaseURL() + hosts;
	}
	
	public static String getNewHostURL() {
		return getBaseURL() + newHost;
	}
}
