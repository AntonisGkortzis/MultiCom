package sharedresources;

/**
 * This class is used to store the commands send through the network
 * This class is grouped by the type of command
 * @author mark
 *
 */
public class Commands { 
	//XXX should this class be combined with Message somehow or is it OK like this?

	private final static String delimiter = ";"; //Character used to separate keywords in commands
	
	/**
	 * Client needs to connect. Send to hosts group
	 */
	public final static String connectRequest = "connect";

	/**
	 * Master needs to ping other hosts to see if they are still alive
	 * TODO just to be sure I made two pings, but maybe one is enough so we can remove one in the future
	 */
	public static String masterPing = "master-ping";
	
	/**
	 * Host needs to ping other hosts to see if master is alive
	 */
	public static String hostPing = "host-ping";
	
	
	public static String constructCommand(String command) {
		return command + delimiter ; //more?
	}
}
