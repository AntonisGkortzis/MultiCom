package sharedresources;


import java.io.Serializable;
import java.net.Socket;

/**
 * This class is used for storing message information
 *
 */
public class Message implements Serializable {

    /**
     * Enumeration to specify to type of process that needs to receive/send
     * @author mark
     */
    public static enum MessageType { //TODO explain in report (globally no details)
        mHostCommand,
        mHostStatus,
        mHostChat,
        mHostVote,
        clientChat,
        clientCommand, //Commands send by clients (initConnection,shutdown, heartbeat)
        mClientCommand,
        hostChat,
        acknowledgement
    }
    
    private static final long serialVersionUID = 1L;
	private MessageType messageType;			// Different kinds of messages
	private boolean command;			//TRUE if the message contains a command. FALSE if the message is ..just a message!
	private String text;				//The text of the message
	private long timestamp;				//The time that the message was sent
	private String username;				//The name of the user sending the message.
	private String processID;            //The id of the process sending the message.
	private long id=0;					//An incrementing number as an id
	private boolean clientAsReceiver;
	private Socket socket;
	private int timesSent;
	
	public Message(){}
	
	public Message(MessageType type, boolean command, String text){
		this.messageType = type;
		this.command = command;
		this.processID = Misc.processID;
		this.text = text;
		this.clientAsReceiver = false;
		this.timesSent = 0;
	}
	
	public Message(MessageType type, boolean command, String username, String text, long id){
		this(type, command, text);
		this.username = username;
		this.id = id;
	}
	
	public void setSocket(Socket socket){
		this.socket = socket;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public MessageType getMessageType() {
		return this.messageType;
	}

	public boolean isCommand(){
		return this.command;
	}
	
    public String getProcessID() {
        return processID;
    }

	public String getUsername(){
		return this.username;
	}
	
	public String getText(){
		return this.text;
	}
	
	public long getId(){
		return this.id;
	}
	
	public void setId(long id) {
	    this.id = id;
	}
	
	public long getTimestamp(){
		return this.timestamp;
	}
	
	public void setTimestamp(long timestamp){
		this.timestamp = timestamp;
	}
	
	public void setText(String text){
		this.text = text;
	}

	public void setMessageType(MessageType type) {
		this.messageType = type;
	}

	public void setProcessId(String processID) {
		this.processID = processID;
	}
	
	public void setClientAsReceiver(boolean flag){
		this.clientAsReceiver = flag;
	}
	public boolean getClientAsReceiver(){
		return this.clientAsReceiver;
	}
	
	@Override
	public String toString(){
		String s = "";
		s = "Id: " + id+ ", Type: " + messageType + ", username: " + username +", processID: " + processID +", Text: " + text;
		
		return s;
	}

	public int getTimesSent() {
		return timesSent;
	}

	public void incTimesSent() {
		this.timesSent++;
	}

	public void setCommand(boolean b) {
		this.command = b;
		
	}

}
