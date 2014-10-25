package sharedresources;

import sharedresources.Message.MessageType;;

/**
 * This class is used for creating and using one MesssageQueue() object throughout the program.
 * 
 */
public class MessageController {
	public MessageQueue queueMHostsCommand = new MessageQueue();
	public MessageQueue queueMHostsStatus = new MessageQueue();
	public MessageQueue queueMHostsChat = new MessageQueue();
	public MessageQueue queueMHostsVote = new MessageQueue();
	public MessageQueue queueHostChat = new MessageQueue();
    
	public MessageQueue queueMClientCommand = new MessageQueue();
	public MessageQueue queueClientChat = new MessageQueue();
	
	// This one is just for sending and NOT for storing received messages
	public MessageQueue queueSend = new MessageQueue();
	
	
	public MessageController(){
//		System.out.println("Creating message controller..");
	}


	public void pushMessageInCorrectQueue(Message receivedMessage) {
    	if(receivedMessage.getMessageType().equals(MessageType.mHostCommand)) {
    	    this.queueMHostsCommand.push(receivedMessage);
    	} else if(receivedMessage.getMessageType().equals(MessageType.mHostChat))  {
    		this.queueMHostsChat.push(receivedMessage);
    	} else if(receivedMessage.getMessageType().equals(MessageType.mHostStatus))  {
    		this.queueMHostsStatus.push(receivedMessage);
    	} else if(receivedMessage.getMessageType().equals(MessageType.mHostVote)) {
    		this.queueMHostsVote.push(receivedMessage);
    	} else if(receivedMessage.getMessageType().equals(MessageType.mClientCommand)) {
    		this.queueMClientCommand.push(receivedMessage);
    	} 

		
	}
	
//	public MessageQueue getQueue(){
//		return queue;
//	}
//	
//	public void push(Message message){
//		queue.push(message);
//	}
//	
//	public Message pop(){
//		return queue.pop();
//	}
}
