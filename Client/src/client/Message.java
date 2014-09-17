package client;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean command;
	private String text;
	
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
