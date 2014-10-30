package sharedresources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;


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
        try {
            this.socket.close();
            this.flag = false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    public void run () {
    	ObjectInputStream inStream;
    	Message message;
		
    	while(flag) {
	    	try {
				inStream = new ObjectInputStream(socket.getInputStream());
				message = (Message)inStream.readObject();
//				System.out.println("@ OneToOneListener received " + message.toString());
				if(Commands.messageIsOfCommand(message, Commands.initOneToOneWithHost)) {
				    //Not further action is needed as this is only a 
				    //message to let the host know that there is a new client.
					addNewClient(message.getProcessID(), message.getUsername());
				} 
				//If a host receives a shutdown message from client
				else if(Commands.messageIsOfCommand(message, Commands.clientShutdown) && this.isHost) {
				    System.out.println("Received a shutdown message from client, closing connection.");
				    inStream.close();
				    this.stop();
				}
				//if the message is a chat message (sent from client to host)
				else if(message.getMessageType().equals(Message.MessageType.hostChat)){ //ONLY HOSTS in this if!
				    //create the acknowledgement and store it in the queueAcknowledgments
				    //Must BE placed before IF, because PID of sender is needed (pid is changed below)
				    String command = Commands.constructCommand(Commands.acknowledgement, message.getProcessID(), Long.toString(message.getId()));
				    Message ack = new Message(Message.MessageType.acknowledgement, true, command);
				    ack.setSocket(socket);
				    messageController.queueAcknowledgements.push(ack); //Comment this if you want to test client retries
				    
				    //Must be after ack
					if(!Commands.messageIsOfCommand(message, Commands.targetedResentMessage)){
						if(this.isHost ){ //Only hosts in if(hostChat) so redundant?
						    message.setProcessId(Misc.processID);
		//				    System.out.println("OneToOneListener received a host chat message " + message.toString());
						    messageController.queueHostChat.push(message); //to send it to the clients connected on this host
						    command = Commands.constructCommand(Commands.forwardMessage, message.getText());
						    Message newMessage = new Message(Message.MessageType.mHostChat,true, message.getUsername(), command, Misc.getNextMessageId());
						    
						    messageController.queueSend.push(newMessage); //Send to other hosts    
						}
					}
				}
				//if the message is an acknowledgment sent by a Host to a Client or vice versa
				else if(message.getMessageType().equals(Message.MessageType.acknowledgement)){
				    if(!this.isHost) {
				        System.out.println("Ack received for message by client: " +message);
				        //remove the original message form the SentMessages queue
				        String processIdOfOriginalSender = Commands.getPidParseTargetedMessageText(message);
				        messageController.queueSentMessagesByClient.remove(processIdOfOriginalSender, Commands.getMessageIdTargetedMessageText(message));
				    } else { //a host can receive an ack from a client
				        removeResendMessageToClient(message);
//                        System.out.println("Ack received for message by host: " +message.toString());				        
				    }
				}
				
				Thread.sleep(250);
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
//				e.printStackTrace();
			    System.out.println("Connection closed.");
				this.stop();
				flag = false;
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
            	System.out.println("@@ OneToOneListener clients: " + forwardMessage.getClients().size() + " ack size: " + messageController.queueSentMessagesByHostToClient.size());
                if(forwardMessage.removeClient(message.getProcessID())) {
                    System.out.println("@@ OneToOneListener (host): Removing client as I received an ack.");
                	iterator.remove();
                }
                break;
            }
        }
    }
    
    private void addNewClient(String processID, String username) {
    	if(!ConnectedClientsList.clientExists(processID)) {
	    	ConnectedClient newclient = new ConnectedClient(processID, username, this.socket);
	        ConnectedClientsList.addClient(newclient); 
	        HostsList.updateHost(Misc.processID, ConnectedClientsList.size(), Config.master);
    	}
    }
}