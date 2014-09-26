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
	
	
	//DELAYS
	
	/**
	 * Delay for pinging other hosts 
	 */
	public final static long DELAY = 500;

}
