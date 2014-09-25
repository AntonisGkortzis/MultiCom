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
	
	//Client to Multiple Hosts
	public final static String connectRequest = "connect";
	
	
	public static String constructCommand(String command) {
		return command + delimiter ; //more?
	}
}
