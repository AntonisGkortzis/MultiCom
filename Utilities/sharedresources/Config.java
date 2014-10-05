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
	public final static String multiCastAddress = "224.0.0.251"; //Mark
//	public final static multiCastAddress = "stub"; //Antonis
	
	/**
	 * Name of the host
	 */
	public final static String hostName = "localhost";
	
	/**
	 * Group for multicast messages to hosts
	 */
	public final static int hostMultiCastGroup = 5000;
	
    public static boolean master = true; // TODO Must be set by election process

	//DELAYS
	
	/**
	 * Delay for pinging other hosts 
	 */
	public final static long DELAY = 3000; //TODO set to correct delay
	
	public static int connectToPortFromHost = -1;

}
