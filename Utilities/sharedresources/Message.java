package sharedresources;


import java.io.Serializable;

import sharedresources.Misc.MessageType;

/**
 * This class is used for storing message information
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private MessageType type;
	private boolean command;			//TRUE if the message contains a command. FALSE if the message is ..just a message!
	private String text;				//The text of the message
	private long timestamp;				//The time that the message was sent
	private String user;				//The name of the user sending the message.
	private String processID;            //The id of the process sending the message.
	private int id=0;					//An incrementing number as an id
	
	public Message(){}
	
	// TODO first three booleans can be replaced by an Enum class which is put in sharedresources
//	boolean hostAsReceiver, boolean hostAsSender, boolean multipleReceivers
	public Message(MessageType type, boolean command, String processID, String user, String text){
		this.type = type;
		this.command = command;
		this.processID = processID;
		this.user = user;
		this.id++;
		this.text = text;
	}
		
	public MessageType getType() {
		return this.type;
	}

	public boolean isCommand(){
		return this.command;
	}
	
    public String getProcessID() {
        return processID;
    }

	public String getUser(){
		return this.user;
	}
	
	public String getText(){
		return this.text;
	}
	
	public int getId(){
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

}
