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
	
	/********KEYWORDS********/
	
	/********ClientToMHost**/
	public final static String initConnection = "AskingPermissionToJoinNetwork";
	
	/****ClientToHost*******/
	public final static String chatMessage = "SendChatMessage";
	public final static String ackForReceive = "SendAckForReceiveMessage";
	
	/**
	 * Client needs to connect. Send to hosts group
	 */
	public final static String connectRequest = "SendConnectRequest";

	/******HostToMHost ********/
	/**
	 * Master needs to ping other hosts to see if they are still alive
	 * TODO just to be sure I made two pings, but maybe one is enough so we can remove one in the future
	 */
//	public static String masterPing = "master-ping";
	
	/**
	 * Host needs to ping other hosts to see if master is alive
	 */
//	public static String hostPing = "host-ping";
	
	/**
	 * Hosts send status updates to other hosts
	 */
	public final static String requestStatusUpdate = "RequestStatusUpdate";
	
	public final static String statusUpdate = "HereIsMyStatusUpdate";
	
	public final static String hostFound = "HostIsFoundForClient";
	
	public final static String forwardMessage = "forwardedMessageReceivedFromClient";
	
	/**
	 * Hosts sending ping request on their connection to the cluster
	 */
	public final static String isAnyoneOutThere = "requestingPingResponse";
	
	public final static String imAlive = "respondToIsAnyoneAlive";
	
	public static String[] splitMessage(Message message) {
	    return message.getText().split(delimiter);
	}
	
	/**
	 * Start an election
	 */
	public final static String startElection = "startElection";
	
	
	/**
	 * Masters needs to find a suitable host for the client
	 */
//	public final static String findHost = "FindSuitableHost";
	public static String constructStatus(int nrOfClients, String address, int port, boolean isMaster, String processID) {
	    return nrOfClients + delimiter + address + delimiter + port + delimiter + isMaster + delimiter + processID + delimiter;
	}
	
	/**
	 * Construct a message with information for the client to connect to a new host
	 * @param host
	 * @param processID ProcessID of the client wanting to connect.
	 * @return
	 */
	public static String constructHostFound(AvailableHost host, String processID ) {
	    return processID + delimiter + host.getAddress() + delimiter + host.getPort();
	}
	
	/**
	 * Constructs commands from keywords
	 * @param command keyword
	 * @return command
	 */
	public static String constructCommand(String command, String information) {
		return command + delimiter + information + delimiter;
	}
	public static String constructCommand(String command) {
	    return command + delimiter;
	}
	
	public static boolean messageIsOfCommand(Message message, String command) {
		String[] messageParts = message.getText().split(delimiter);
		return messageParts[0].equals(command);
	}
    public static AvailableHost getStatus(Message message) {
        String[] messageParts = message.getText().split(delimiter);
        int nrOfClients = Integer.parseInt(messageParts[1]);
        String address = messageParts[2];
        int port = Integer.parseInt(messageParts[3]);
        boolean isMaster = Boolean.parseBoolean(messageParts[4]);
        String processID = messageParts[5];

        return new AvailableHost(nrOfClients, address, port, isMaster, processID);
    }
}
