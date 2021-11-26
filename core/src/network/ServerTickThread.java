package network;

public class ServerTickThread extends Thread {
	
	ServersideThread main;
	boolean end = false;
	
	public ServerTickThread(ServersideThread main) {
		this.main = main;
	}
	
	public void run() {
		do {
			try {
				main.serverTick();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("[SERVER] Error during tick");
			}
		}while(!end);
		
	}

}
