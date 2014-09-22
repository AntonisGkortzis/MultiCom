package sharedresources;


import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean host; //true if the message's destination is a host
	private boolean command;
	private String text;
	private int timestamp;
	private String user;
	
	public Message(){}
	
	public Message(boolean command, String text){
		this.command = command;
		this.text= text;
	}
	
	public boolean isCommand(){
		return this.command;
	}
	
	public String getMessage(){
		return this.text;
	}
}
