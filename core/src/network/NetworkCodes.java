package network;

public abstract class NetworkCodes {
	
	public static final int CODELENGTH = 6;
	
	//Network codes are 5 characters long plus a dash (-). After the dash, arguments come.
	//So for example, a connect message is composed of the network code "CNNCT-" and the argument is the username. ( "CNNCT-SirTosky" )
	//would be a network code to connect, and the argument would be the username, SirTosky.
	//Sometimes, there might be multiple arguments. They are divided by a slash (/).
	
	
	public static final String CONNECT = "CNNCT-";
	public static final String DISCONNECT = "DCNCT-";
	public static final String INPUT = "INPUT-";
	public static final String FORBIDDEN = "FRBDN-";
	public static final String ERROR = "ERROR-";
	public static final String PING = "PIING-";
	public static final String PONG = "CPOONG-";
	public static final String TANKSYNC = "TSYNC-"; //TankSync. The format is TSYNC-PosX/PosY/Rotation
	public static final String NEWSPRITE   = "SPRITE-";
	public static final String REMOVESPRITE  = "NORITE-"; //Cuack.
	public static final String EXPLOSION = "BOOOM-";
	


}
