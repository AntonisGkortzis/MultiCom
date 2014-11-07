package sharedresources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;

import utils.CRC32Calculator;

/**
 * This class is used for a listener of a one to one connection.
 * 
 */
public class OneToOneListener implements Runnable {
    private Socket socket;
    private MessageController messageController;
    private Thread t;
    private boolean isHost;
	
	public static long messageId = 0;
	boolean flag = true;
    
    public OneToOneListener(Socket socket, MessageController messageController, boolean isHost) {
    	this.socket = socket;
        this.messageController = messageController;
        this.isHost = isHost;
    }

    public void start() {
		t = new Thread(this);
		t.start();
    }

    public void stop() {
        this.flag = false;
    }
    
    public void run () {
    	ObjectInputStream inStream;
    	Message message;
		
    	while(flag) {
	    	try {
	    	    if(socket==null) {
	    	        return;
	    	    }
				inStream = new ObjectInputStream(socket.getInputStream());
				message = (Message)inStream.readObject();
				if(message.getMessageType().equals(Message.MessageType.clientCommand)) {
    				if(Commands.messageIsOfCommand(message, Commands.initOneToOneWithHost)) {
    				    //Not further action is needed as this is only a 
    				    //message to let the host know that there is a new client.
    					addNewClient(message.getProcessID(), message.getUsername());
    				} 
    				//If a host receives a shutdown message from client
    				else if(Commands.messageIsOfCommand(message, Commands.clientShutdown)) {
    				    System.out.println("##-- Received a shutdown message from Messenger " + message.getProcessID() + " --##");
    				    //Remove clients since it has shutdown
    				    ConnectedClientsList.removeClient(message.getProcessID());
    				    inStream.close();
    				    this.stop();
    				}
    				//Update last seen of a client (ClientMonitor monitors clients)
    				else if(Commands.messageIsOfCommand(message, Commands.clientHeartbeat)) {
    				    ConnectedClientsList.updateClient(message.getProcessID());
    				}
				}
				//if the message is a chat message (sent from client to host)
				else if(message.getMessageType().equals(Message.MessageType.hostChat)//ONLY HOSTS in this if!
						&& message.getChecksum() == CRC32Calculator.getChecksum(message.getText())){//Create an acknowledgment only if the message checksum is the same as the messages text 
				    //create the acknowledgement and store it in the queueAcknowledgments
				    //Must BE placed before IF, because PID of sender is needed (pid is changed below)
				    String command = Commands.constructCommand(Commands.acknowledgement, message.getProcessID(), Long.toString(message.getId()));
				    Message ack = new Message(Message.MessageType.acknowledgement, command);
				    ack.setSocket(socket);
				    //TODO DEMO: Test Messenger Retries
				    messageController.queueAcknowledgements.push(ack);
				    
				    //Must be after ack
					if(!Commands.messageIsOfCommand(message, Commands.targetedResentMessage)){
						if(this.isHost ){ //Only hosts in if(hostChat) so redundant?
						    //Hold back queue
						    message.setTimeReceived(new Date().getTime());
						    ConnectedClientsList.addHoldBackMessage(message);
						}
					}
				}
				//if the message is an acknowledgment sent by a Host to a Client or vice versa
				else if(message.getMessageType().equals(Message.MessageType.acknowledgement)){
				    if(!this.isHost) {
				        //remove the original message form the SentMessages queue
				        String processIdOfOriginalSender = Commands.getPidParseTargetedMessageText(message);
				        messageController.queueSentMessagesByClient.remove(processIdOfOriginalSender, Commands.getMessageIdTargetedMessageText(message));
				    } else { //a host can receive an ack from a client
				        removeResendMessageToClient(message);
				    }
				}
				
				Thread.sleep(250);
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
			    System.out.println("##-- Connection closed --##");
				this.stop();
			}
    	}
    }
    
    /**
     * Remove the client for a message as the client has sent an acknowledgement.
     * @param message
     */
    private void removeResendMessageToClient(Message message) {
    	Iterator<ForwardMessage> iterator = messageController.queueSentMessagesByHostToClient.iterator();
        while(iterator.hasNext()) {
        	ForwardMessage forwardMessage = iterator.next();
            if(forwardMessage.getId() == Commands.getOriginalId(message)) { //correct message
                if(forwardMessage.removeClient(message.getProcessID())) {
                	iterator.remove();
                }
                break;
            }
        }
    }
    
    private void addNewClient(String processID, String username) {
    	if(!ConnectedClientsList.clientExists(processID)) {
	    	ConnectedClient newclient = new ConnectedClient(processID, username);
	        ConnectedClientsList.addClient(newclient); 
    	}
    }
}