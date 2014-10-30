package sharedresources;

/**
 * Class used for storing shared config variables 
 * @author mark
 *
 */
public final class Config {
	
	/**
	 * Address used for multicast
	 */
	public final static String multiCastAddress = "224.0.0.251";
	
	/**
	 * Name of the host
	 */
	public final static String hostName = "localhost";
	
	/**
	 * Group for multicast messages to hosts
	 */
	public final static int hostMultiCastGroup = 5000;
	
    public static boolean master = false; // TODO Must be set by election process

	//DELAYS
	
	/**
	 * Delay for pinging other hosts 
	 */
	public final static int DELAY = 3000; //TODO set to correct delay
	
	/**
	 * Delay of sending heartbeats to host by client
	 */
	public final static int clientHeartbeatDelay = 2500;
	
	public static int connectToPortFromHost = -1;

}
