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
	public final static String initOneToOneWithHost = "IAmNowConnectedToYou";
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
	
//	/**
//	 * Masters needs to find a suitable host for the client
//	 */
	public final static String hostFound = "HostIsFoundForClient";
	
	public final static String forwardMessage = "forwardedMessageReceivedFromClient";
	
	public final static String targetedResentMessage = "messageThatShouldBeParsedOnlyByTheUserThatHasTheSameProcessIdAsTheMessages";
	
	/**
	 * Hosts sending ping request on their connection to the cluster
	 */
	//TODO uncomment, but needed?
//	public final static String isAnyoneOutThere = "requestingPingResponse"; //TODO this is probably requestStatusUpdate
	
//	public final static String imAlive = "respondToIsAnyoneAlive"; //TODO this is probably StatusUpdate

	
	public final static String acknowledgement = "containerOfAMessageAcknowledgement";
	
	public static String[] splitMessage(Message message) {
	    return message.getText().split(delimiter);
	}
	
	/**
	 * Start an election
	 */
	public final static String startElection = "startElection";
	
	public final static String vote = "voteFor";
	
	public final static String IAmTheMaster = "announcementOfYourselfAsTheMaster";
	
	public static String constructStatus(int nrOfClients, String address, int port, boolean isMaster) {
	    return nrOfClients + delimiter + address + delimiter + port + delimiter + isMaster + delimiter;
	}
	
	/**
	 * Construct a message with information for the client to connect to a new host
	 * @param host
	 * @param processID ProcessID of the client wanting to connect.
	 * @return
	 */
	public static String constructHostFound(Host host, String processID ) {
	    return processID + delimiter + host.getAddress() + delimiter + host.getPort();
	}
	
	
	/**
	 * Save the PID and start time of the starter who starts the election
	 * for every election message (first set when constructing startElection)
	 * @param host
	 * @return
	 */
	public static String constructElectionMessage(String command, String processID, long time) {
		return command + delimiter + processID + delimiter + time;
	}
	/**
	 * Constructs commands from keywords
	 * @param command keyword
	 * @return command
	 */
	public static String constructCommand(String command, String processID, String information) {
		return command + delimiter + processID + delimiter + information + delimiter;
	}
	
	public static String constructCommand(String command, String information) {
		return command + delimiter + information + delimiter;
	}
	
	public static String constructCommand(String command) {
	    return command + delimiter;
	}
	
	public static String constructVoteCommand(String command,
			String starterProcessID, Long time, String vote) {
		return command + delimiter + starterProcessID + delimiter + time + delimiter + vote;
	}
    public static Host getStatus(Message message) {
        String[] messageParts = message.getText().split(delimiter);
        int nrOfClients = Integer.parseInt(messageParts[1]);
        String address = messageParts[2];
        int port = Integer.parseInt(messageParts[3]);
        boolean isMaster = Boolean.parseBoolean(messageParts[4]);
        String processID = message.getProcessID();

        return new Host(nrOfClients, address, port, isMaster, processID);
    }
    
    public static boolean messageIsOfCommand(Message message, String command) {
//        String[] messageParts = message.getText().split(delimiter);
//        return messageParts[0].equals(command);
        return getMessagePart(message, 0).equals(command);
    }
    
    public static String getVote(Message message){
//        String[] messageParts = message.getText().split(delimiter);
//        return messageParts[3];
        return getMessagePart(message, 3);
    }

	public static String getStarterProcessID(Message message) {
//		String[] messageParts = message.getText().split(delimiter);
//		return messageParts[1];
	    return getMessagePart(message, 1);

	}

	public static long getStarterTime(Message message) {
//		String[] messageParts = message.getText().split(delimiter);
//		return Long.parseLong(messageParts[2]);
	    return Long.parseLong(getMessagePart(message, 2));

	}
	
	public static long getOriginalId(Message message){
//		String[] messageParts = message.getText().split(delimiter);
//		return Long.parseLong(messageParts[1]);
	    return Long.parseLong(getMessagePart(message, 1));
	}
	
	private static String getMessagePart(Message message, int i) {
	    String[] messageParts = message.getText().split(delimiter);
        return messageParts[i];
	}

	public static String getParseMessageText(Message message) {
		return getMessagePart(message, 1);
	}
	
	public static String getParseTargetedMessageText(Message message) {
		return getMessagePart(message, 2);
	}
}
