package network;

import java.net.InetAddress;

import input.Client;

public class ServerClient extends Client{
	
	public InetAddress IP;
	public int port;
	public long lastSync;
	public long lastTick;
	
	public ServerClient(InetAddress ip, int port) {
		this.IP = ip;
		this.port = port;
	}
	

}

