package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.bws.tankshost.ServerConfig;

import elements.ClientSprite;
import elements.Tank;
import input.InputKeys;
import utilities.ClientRender;

public class ServersideThread extends Thread {

	private long serverTick;
	private long lastPing;
	public int spriteIDCounter;
	ServerTickThread tickThread;
	int idCounter = 0; // Tank ID Counter
	private DatagramSocket socket;
	private boolean end, serverCreated = false;
	int socketPort = ServerConfig.DEFAULT_PORT;

	private int connectedClientCounter = 0;
	private ServerClient[] clients = new ServerClient[ServerConfig.MAX_CLIENTS];
	

	public ServersideThread() {
		serverCreated = startServer();
	}

	private boolean startServer() { // attempt to create a datagram socket.
		try {
			socket = new DatagramSocket(socketPort);
			System.out.println("[SERVER] Socket established on port: " + socketPort);
			spriteIDCounter = 0;
			serverCreated = true;
			return true; // server created.
		} catch (SocketException e) {
			// e.printStackTrace(); too much spam
			System.out.println("[SERVER] Unable to create socket on port " + socketPort + ".");
			return false; // unable to create
		}
	}

	public void stopServer() {
		broadcast(NetworkCodes.DISCONNECT + "Server closed.");
		if (!socket.isClosed()) {
			System.out.println("[SERVER] Closing socket on port " + socketPort); // Close socket currently in use.
			socket.close();
		}
		this.interrupt();// close the thread.
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	private void checkSocket() {// TODO: Make sure the socket check doesn't become an infinite loop.
		// while (!serverCreated) { //If the socket is unexistant.

		serverCreated = startServer(); // attempt to create a socket.
		try {
			Thread.sleep(1000); // wait a second before checking again.
			socketPort++;
			startServer();
		} catch (InterruptedException e) {
			e.printStackTrace();
			startServer();// retry anyways.
		}
		// }//do not continue until it is created.
	}

	public void serverTick() {
		// Check that all clients have been responsive.
		ServerClient[] connectedClients = getConnectedClients();
		for (int i = 0; i < connectedClients.length; i++) {
			int tickDifference = (int) (serverTick - connectedClients[i].lastTick);
			if (tickDifference > 150) { // If the user didn't pong or send messages for 200 ticks, disconnect.
				disconnectClient(i);
			}
		}
		// Check if clients should be pinged.
		//TODO: Ping must send render list length, HP, reload time.
		if (serverTick - lastPing >= ServerConfig.PING_RATE) {// If there's been enough ticks between last ping.
			broadcast(NetworkCodes.PING);
		}
		// Sync tanks with server.
		syncSpriteData();

		// Finish server tick
		serverTick++;
	}

	@Override
	public void run() {
		while (!serverCreated) {
			checkSocket(); // Make sure a socket actually exists before anything else.
		}
		do {
			if (socket.isClosed()) {
				end = true;
			}
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
				processMessage(packet);
			} catch (IOException e) {
				if (!socket.isClosed()) { // ignore error if the socket was intentionally closed.
					System.out.println("[SERVER] Socket Exception: Could not receive packet.");
				}
				// e.printStackTrace();
			}
		} while (!end);
	}

//////////Messaging////////////////////////////////////////
	private void processMessage(DatagramPacket packet) {
		String msg = new String(packet.getData()).trim();
		String networkCode = msg.substring(0, NetworkCodes.CODELENGTH); // The first part of the message is the network
																		// code.
		String args = msg.substring(NetworkCodes.CODELENGTH, msg.length()); // Everything after the network code are the
																			// arguments (args) of the network message.

		if (!isClient(packet.getAddress()) && !networkCode.equals(NetworkCodes.CONNECT)) {
			sendMessage(NetworkCodes.FORBIDDEN + "Not connected to server.", packet.getAddress(), packet.getPort());
			return;
		}
		if (!networkCode.equals(NetworkCodes.PONG)) {
			System.out.println("[SERVER RECEIVED]" + msg);
		}

		switch (networkCode) { // switches the network code.
		case NetworkCodes.CONNECT: // connect
			handleConnection(packet, args);
			break;
		///
		case NetworkCodes.DISCONNECT: // disconnect
			handleDisconnection(packet, args);
			break;
		///
		case NetworkCodes.INPUT:
			handleUserInput(packet, args);
			break;
		///
		case NetworkCodes.PONG:

			break;
		///
		default:
			sendMessage(NetworkCodes.ERROR + "Invalid network code.", packet.getAddress(), packet.getPort());
			break;
		}

		if (!networkCode.equals(NetworkCodes.DISCONNECT)) {
			ServerClient currentClient = clients[getClientID(packet.getAddress())];
			currentClient.lastTick = serverTick; // acknowledge the client who ticked the server.
		}

	}

	public void sendMessage(String msg, InetAddress ip, int port) {
		byte[] data = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadcast(String msg) { // send message to all connected clients.
		ServerClient[] connectedClients = getConnectedClients();
		for (int i = 0; i < connectedClients.length; i++) {
			sendMessage(msg, clients[i].IP, clients[i].port);
		}
	}

//////////////Client handling////////////////////////////////////

	public boolean isClient(InetAddress ip) {
		for (int i = 0; i < clients.length; i++) {
			if (clients[i] != null && clients[i].IP == ip) {
				return true;
			}
		}
		return false;
	}

	public int getClientID(InetAddress ip) {
		for (int i = 0; i < clients.length; i++) {
			if (clients[i].IP == ip) {
				return i;
			}
		}
		return -1;
	}

	public boolean usernameInUse(String username) {
		for (int i = 0; i < clients.length; i++) {
			if (clients[i] != null && clients[i].username == username) {
				return true;
			}
		}
		return false;
	}

	public ServerClient addClient(InetAddress ip, int port, String username) {
		ServerClient newClient;
		for (int i = 0; i < clients.length; i++) {
			if (clients[i] == null) {
				newClient = new ServerClient(ip, port);
				newClient.username = username;
				clients[i] = newClient;
				connectedClientCounter++;
				return newClient;
			}
		}
		return null;

	}

	public void removeClient(int id) {
		// TODO: Consider some kind of dispose() ?
		clients[id] = null;
		connectedClientCounter--;

	}

	public void disconnectClient(int index) {
		sendMessage(NetworkCodes.DISCONNECT, clients[index].IP, clients[index].port);
		removeClient(index);
	}

	public ServerClient[] getConnectedClients() {
		ServerClient[] connectedClients = new ServerClient[connectedClientCounter];
		int fillCount = 0;
		for (int i = 0; i < ServerConfig.MAX_CLIENTS; i++) {
			if (clients[i] != null) {
				connectedClients[fillCount] = clients[i];
				fillCount++;
			}
			if (fillCount >= connectedClientCounter) {
				break;
			}
		}
		return connectedClients;
	}

	//////////// processMessage functions//////////////////////////////////////////
	private void handleConnection(DatagramPacket packet, String args) {
		if (isClient(packet.getAddress())) {// If the client was already connected, just tell them they connected so
											// they can sync.
			sendMessage(NetworkCodes.CONNECT + "Already connected.", packet.getAddress(), packet.getPort());
		} else if (usernameInUse(args)) {
			sendMessage(NetworkCodes.ERROR + "Username in use.", packet.getAddress(), packet.getPort());
		} else if (connectedClientCounter < ServerConfig.MAX_CLIENTS) {
			ServerClient newClient = addClient(packet.getAddress(), packet.getPort(), args);
			sendMessage(NetworkCodes.CONNECT + "Connected as " + args, packet.getAddress(), packet.getPort());
			System.out.println("[SERVER] " + args + " connected.");
		} else {
			sendMessage(NetworkCodes.ERROR + "Server full.", packet.getAddress(), packet.getPort());
		}
	}

	private void handleDisconnection(DatagramPacket packet, String msg) {
		removeClient(getClientID(packet.getAddress()));
	}

	private void handleUserInput(DatagramPacket packet, String packagedArgs) { // packaged args is the string with
																				// multiple arguments divided with /
		String[] args = packagedArgs.split("/");
		ServerClient requestingClient = clients[getClientID(packet.getAddress())];
		// Below: Modify the user input keys according to the network message. (huh?)
		requestingClient.inputs.replace(InputKeys.valueOf(args[0]), !Boolean.parseBoolean(args[1]),
				Boolean.parseBoolean(args[1]));
	}

/////////////////SPRITE MANAGER
	
	public int generateSpriteID() {
		spriteIDCounter++;
		return spriteIDCounter;
	}
	
	private String getSpriteData(ClientSprite sprite) {
		return sprite.getRoute()+"/"+sprite.getID()+"/"+sprite.getX()+"/"+sprite.getY()+"/"+sprite.getRotation()+"/"+sprite.getWidth()+"/"+sprite.getHeight();
	}
	
	public void addSprite(ClientSprite sprite) {
		broadcast(NetworkCodes.NEWSPRITE+getSpriteData(sprite) );
	}
	
	public void removeSprite(ClientSprite sprite) {
		broadcast(NetworkCodes.REMOVESPRITE+sprite.getID());
	}

	private void syncSpriteData() {
		for (int i = 0; i < ClientRender.renderList.size(); i++) {
			ClientSprite sprite = ClientRender.renderList.get(i);
			broadcast(NetworkCodes.UPDATESPRITE+getSpriteData(sprite));
		}
	}
	
	private void syncRenderList(ServerClient client) {//For when a player's renderList is desynced.
		for (int i = 0; i < ClientRender.renderList.size(); i++) {
			ClientSprite sprite = ClientRender.renderList.get(i);
			sendMessage(NetworkCodes.NEWSPRITE+getSpriteData(sprite),client.IP,client.port);
		}
		
	}

}
