package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.bws.tankshost.ServerConfig;

import elements.ClientSprite;
import elements.Tank;
import input.InputKeys;
import utilities.Render;

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
	private ArrayList<ServerClient> clients = new ArrayList<ServerClient>();

	public ServersideThread() {
		serverCreated = startServer();
	}

	private boolean startServer() { // attempt to create a datagram socket.
		try {
			socket = new DatagramSocket(socketPort);
			System.out.println("[SERVER] Socket established on port: " + socketPort);
			spriteIDCounter = 0;
			Render.setServerThread(this);
			serverCreated = true;
			return true; // server created.
		} catch (SocketException e) {
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
			startServer();// retry anyways.
		}
		// }//do not continue until it is created.
	}

	public void serverTick() {
		checkTimeout();
		syncSpriteData();
		ping();
		serverTick++;
	}

	private void checkTimeout() {
		for (int i = 0; i < clients.size(); i++) {
			int tickDifference = (int) (serverTick - clients.get(i).lastTick);
			if (tickDifference > 150) { // If the user didn't pong or send messages for 200 ticks, disconnect.
				disconnectClient(i);
			}
		}
	}

	private void ping() {
		if (serverTick - lastPing >= ServerConfig.PING_RATE) {// If there's been enough ticks between last ping.
			broadcast(NetworkCodes.PING+Render.renderList.size()+"-"+serverTick);
		}
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

		if (!networkCode.equals(NetworkCodes.CONNECT) && !isClient(packet.getAddress())) {
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

		if (!networkCode.equals(NetworkCodes.DISCONNECT) && clients.size() > 0) {
			ServerClient currentClient = clients.get(getClientID(packet.getAddress()));
			currentClient.lastTick = serverTick; // acknowledge the client who ticked the server.
		}

	}

	public void sendMessage(String msg, InetAddress ip, int port) {
		byte[] data = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
		}
	}

	public void broadcast(String msg) { // send message to all connected clients.
		for (int i = 0; i < clients.size(); i++) {
			sendMessage(msg, clients.get(i).IP, clients.get(i).port);
		}
	}

//////////////Client handling////////////////////////////////////

	public boolean isClient(InetAddress ip) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).IP == ip) {
				return true;
			}
		}
		return false;
	}

	public int getClientID(InetAddress ip) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).IP == ip) {
				return i;
			}
		}
		return -1;
	}

	public boolean usernameInUse(String username) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).username == username) {
				return true;
			}
		}
		return false;
	}

	public ServerClient addClient(InetAddress ip, int port, String username) {
		ServerClient newClient;
		newClient = new ServerClient(ip, port);
		newClient.username = username;
		clients.add(newClient);
		createTank(newClient);
		return newClient;

	}

	public void removeClient(int id) {
		// TODO: Consider some kind of dispose() ?
		removeTank(clients.get(id));
		clients.remove(id);

	}

	public void disconnectClient(int index) {
		sendMessage(NetworkCodes.DISCONNECT, clients.get(index).IP, clients.get(index).port);
		removeClient(index);
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
		String[] args = packagedArgs.split("-");
		ServerClient requestingClient = clients.get(getClientID(packet.getAddress()));
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
	
		return sprite.getRoute() + "-" + sprite.getID() + "-" + sprite.getX() + "-" + sprite.getY() + "-"
				+ sprite.getRotation() + "-" + sprite.getWidth() + "-" + sprite.getHeight() + "-" + sprite.getOriginX()
				+ "-" + sprite.getOriginY();
		
	}

	public void addSprite(ClientSprite sprite) {
		broadcast(NetworkCodes.NEWSPRITE + getSpriteData(sprite));
	}

	public void removeSprite(ClientSprite sprite) {
		broadcast(NetworkCodes.REMOVESPRITE + sprite.getID());
	}

	private void syncSpriteData() {
		for (int i = 0; i < Render.renderList.size(); i++) {
			ClientSprite sprite = Render.renderList.get(i);
			broadcast(NetworkCodes.UPDATESPRITE + getSpriteData(sprite));
		}
	}

	public void doExplosion(float x, float y) {
		broadcast(NetworkCodes.EXPLOSION + x + "-" + y);
	}

//////TANK MANAGEMENT
	public static void createTank(final ServerClient client) {
		Gdx.app.postRunnable(new Runnable() {
			public void run() {
				Render.tanks.add(new Tank(client));
			}
		});

	}

	public static void removeTank(final ServerClient client) {
		Gdx.app.postRunnable(new Runnable() {
			public void run() {
				for (int i = 0; i < Render.tanks.size(); i++) {
					if (Render.tanks.get(i).owner == client) {
						Render.tanks.get(i).destroy();
						Render.tanks.remove(i);
						break;
					}
				}
			}
		});
	}

}
