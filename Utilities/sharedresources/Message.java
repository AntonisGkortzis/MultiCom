package sharedresources;


import java.io.Serializable;

import com.sun.security.ntlm.Client;

/**
 * This class is used for storing message information
 *
 */
public class Message implements Serializable {

    /**
     * Enumeration to specify to type of process that needs to receive/send
     * @author mark
     */
    public static enum MessageType { //TODO explain in report
        mHostCommand,
        mHostStatus,
        mHostChat,
        mHostVote,
        clientChat,
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
	private long id=0;					//An incrementing number as an id //TODO used for what?
	private boolean clientAsReceiver;
	
	public Message(){}
	
	public Message(MessageType type, boolean command, String text){
		this.messageType = type;
		this.command = command;
		this.processID = Misc.processID;
//		this.id++;
		this.text = text;
		this.clientAsReceiver = false;
	}
	
	public Message(MessageType type, boolean command, String username, String text, long id){
		this(type, command, text);
		this.username = username;
		this.id = id;
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
	
	public String toString(){
		String s = "";
		s = "Id: " + id+ ", Type: " + messageType +", processID: " + processID +", Text: " + text;
		
		return s;
	}

}
