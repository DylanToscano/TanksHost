package network;

import java.net.DatagramSocket;

public class Serverside {
	
	private static ServersideThread hs;
	private static ServerTickThread tickThread;
	
	public Serverside() {
		hs = new ServersideThread();
		tickThread = new ServerTickThread(hs);
		hs.start();
		tickThread.start();
		
	}

	public static ServersideThread getHs() {
		return hs;
	}
	
	public static DatagramSocket getSocket() {
		return(hs.getSocket() );
	}
}
