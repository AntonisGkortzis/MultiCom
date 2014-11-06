package sharedresources;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
	
	// This one is just for sending and NOT for storing received messages
	public MessageQueue queueSend = new MessageQueue();
	
	//Stores the created (ready to be sent) acknowledgments 
	//This queue is used by both Hosts and Clients. NOT at the same time!
	public MessageQueue queueAcknowledgements = new MessageQueue();
	//Stores the sent messages by clients. These messages will be removed after receiving their acknowledgment. Otherwise sent again
	public MessageQueue queueSentMessagesByClient = new MessageQueue();
	//Stores the sent messages by hosts to its clients.
	public BlockingQueue<ForwardMessage> queueSentMessagesByHostToClient = new LinkedBlockingQueue<ForwardMessage>();
	//Stores the sent messages by hosts to its hosts.
	public BlockingQueue<ForwardMessage> queueSentMessagesByHostToMHost = new LinkedBlockingQueue<ForwardMessage>();
	//Stores the messages that the Clients receives. Later these messages are popped and presented to the Client.
	public MessageQueue queueClientReceivedMessages = new MessageQueue();
	//Stores the received acknowledgments sent by hosts to other hosts for mHostChat messages
	private MessageQueue queueAcknowledgementsFromMHosts = new MessageQueue();
	

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
    	} else if(receivedMessage.getMessageType().equals(MessageType.hostChat)) {
    	    this.queueHostChat.push(receivedMessage);
    	} else if(receivedMessage.getMessageType().equals(MessageType.acknowledgement)) {
    		this.queueAcknowledgementsFromMHosts.push(receivedMessage);
    	}
	}
	
}
