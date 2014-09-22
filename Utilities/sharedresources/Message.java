package sharedresources;


import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean hostAsReceiver;		//TRUE if the message's destination is a host.
	private boolean hostAsSender; 		//TRUE if the message's sender is a host.
	private boolean multipleReceivers; 	//TRUE if the message should be delivered to a group of listeners.
	private boolean command;			//TRUE if the message contains a command. FALSE if the message is ..just a message!
	private String text;				//The text of the message
	private long timestamp;				//The time that the message was sent
	private String user;				//The name of the user sending the message.
	private int id=0;					//An incrementing number as an id
	
	public Message(){}
	
	public Message(boolean hostAsReceiver, boolean hostAsSender, boolean multipleReceivers, boolean command, String user, String text){
		this.hostAsReceiver = hostAsReceiver;
		this.hostAsSender = hostAsSender;
		this.multipleReceivers = multipleReceivers;
		this.command = command;
		this.user = user;
		this.id++;
		this.text = text;
	}
	
	public boolean isHostTheReceiver(){
		return this.hostAsReceiver;
	}
	
	public boolean isHostTheSender(){
		return this.hostAsSender;
	}
	
	public boolean hasMultipleReceivers(){
		return this.multipleReceivers;
	}
	
	public boolean isCommand(){
		return this.command;
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
	
}
