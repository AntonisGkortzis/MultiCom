package sharedresources;


import java.io.Serializable;

import sharedresources.Misc.MessageType;

/**
 * This class is used for storing message information
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private MessageType messageType;			// Different kinds of messages
	private boolean command;			//TRUE if the message contains a command. FALSE if the message is ..just a message!
	private String text;				//The text of the message
	private long timestamp;				//The time that the message was sent
	private String username;				//The name of the user sending the message.
	private String processID;            //The id of the process sending the message.
	private int id=0;					//An incrementing number as an id
	
	public Message(){}
	
	public Message(MessageType type, boolean command, String processID, String username, String text){
		this.messageType = type;
		this.command = command;
		this.processID = processID;
		this.username = username;
		this.id++;
		this.text = text;
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
