package sharedresources;

/**
 * This class is used to store the commands send through the network
 * This class is grouped by the type of command
 * @author mark
 *
 */
public class Commands { 

	private final static String delimiter = ";"; //Character used to separate keywords in commands
	
	/********KEYWORDS********/
	
	/********ClientToMHost**/
	public final static String initConnection = "AskingPermissionToJoinNetwork";
	
	/****ClientToHost*******/
	public final static String initOneToOneWithHost = "IAmNowConnectedToYou";
	public final static String clientHeartBeat = "IAmStillAlive";
	public final static String chatMessage = "SendChatMessage";
	public final static String ackForReceive = "SendAckForReceiveMessage";
	public final static String clientShutdown = "IHaveShutdown";
	/**
	 * Client needs to connect. Send global multicast
	 */
	public final static String connectRequest = "SendConnectRequest";

	/******HostToMHost ********/
	/**
	 * Hosts send status updates to other hosts
	 */
	public final static String requestStatusUpdate = "RequestStatusUpdate";
	
	public final static String statusUpdate = "HereIsMyStatusUpdate";
	
	/**
	 * Masters needs to find a suitable host for the client
	 */
	public final static String hostFound = "HostIsFoundForClient";
	
	public final static String forwardMessage = "forwardedMessageReceivedFromClient";
	
	public final static String targetedResentMessage = "messageThatShouldBeParsedOnlyByTheUserThatHasTheSameProcessIdAsTheMessages";
	
	
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
        return getMessagePart(message, 0).equals(command);
    }
    
    public static String getVote(Message message){
        return getMessagePart(message, 3);
    }

	public static String getStarterProcessID(Message message) {
	    return getMessagePart(message, 1);
	}

	public static long getStarterTime(Message message) {
	    return Long.parseLong(getMessagePart(message, 2));
	}
	
	public static long getOriginalId(Message message){
	    return Long.parseLong(getMessagePart(message, 1));
	}
	
	private static String getMessagePart(Message message, int i) {
	    String[] messageParts = message.getText().split(delimiter);
        return messageParts[i];
	}

	public static String getParseMessageText(Message message) {
		return getMessagePart(message, 1);
	}
	
	public static String getPidParseTargetedMessageText(Message message) {
	    return getMessagePart(message, 1);
	}
	
	public static long getMessageIdTargetedMessageText(Message message) {
	    return Long.parseLong(getMessagePart(message, 2));
	}
	
	public static String getTextParseTargetedMessageText(Message message) {
		return getMessagePart(message, 2);
	}
}
