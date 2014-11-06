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
	
    public static boolean master = false;

	//DELAYS
	
	/**
	 * Delay for pinging other hosts 
	 */
	public final static int DELAY = 3000;
	
	/**
	 * Delay of sending heartbeats to host by client
	 */
	public final static int clientHeartbeatDelay = 2500;
	
	/**
	 * Delay of sending heartbeat to MClients by host
	 */
	public final static int hostHeartbeatDelay = 1000;
	
	/**
	 * Amount of milliseconds messages should wait in hold-back queue, 
	 * the bigger the delay the bigger the delay in receiving messages
	 */
    public final static long holdBackQueueDelay = 300;

	public static int connectToPortFromHost = -1;

}
