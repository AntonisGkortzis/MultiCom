package sharedresources;

import java.util.ArrayList;
import java.util.List;

import sharedresources.Message.MessageType;;

/**
 * This class is used for creating and using MesssageQueue() objects throughout the program.
 * Messages will be placed in the correct queue by pushMessageInCorrectQueue
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
	
	//Stores the created (ready to be sent) acknowledgments 
	public MessageQueue queueAcknowledgements = new MessageQueue();
	//Stores the sent messages by clients. These messages will be removed after receiving their acknowledgment. Otherwise sent again
	public MessageQueue queueSentMessagesByClient = new MessageQueue();
	//Stores the sent messages by hosts to its clients.
	public List<ForwardMessage> queueSentMessagesByHostToClient = new ArrayList<ForwardMessage>();
	

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
	
}
