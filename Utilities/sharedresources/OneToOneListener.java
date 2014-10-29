package sharedresources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


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
    
    public OneToOneListener(Socket socket, MessageController messageController, boolean isHost) {
    	this.socket = socket;
        this.messageController = messageController;
        this.isHost = isHost;
    }

    public void start() {
		t = new Thread(this);
		t.start();
    }
    
    public void run () {
    	ObjectInputStream inStream;
    	Message message;
    	boolean flag = true;
		
    	while(flag) {
	    	try {
				inStream = new ObjectInputStream(socket.getInputStream());
				message = (Message)inStream.readObject();
				System.out.println("@ OneToOneListener received " + message.toString());
				if(Commands.messageIsOfCommand(message, Commands.initOneToOneWithHost)) {
				    //Not further action is needed as this is only a 
				    //message to let the host know that there is a new client.
					addNewClient(message.getProcessID(), message.getUsername());
				}
				//if the message is a chat message (sent from client to host)
				else if(message.getMessageType().equals(Message.MessageType.hostChat) && this.isHost){
				    message.setProcessId(Misc.processID);
//				    System.out.println("OneToOneListener received a host chat message " + message.toString());
				    messageController.queueHostChat.push(message); //to send it to the clients connected on this host
				    String command = Commands.constructCommand(Commands.forwardMessage, message.getText());
				    Message newMessage = new Message(Message.MessageType.mHostChat,true, message.getUsername(), command, Misc.getNextMessageId());
				    
				    messageController.queueSend.push(newMessage); //Send to other hosts
				    
				    //create the acknowledgement and store it in the queueAcknowledgments
				    command = Commands.constructCommand(Commands.acknowledgement, Long.toString(message.getId()));
				    Message ack = new Message(Message.MessageType.acknowledgement, true, command);
				    ack.setSocket(socket);
				    System.out.println("Pushing ack " + ack.toString());
				    messageController.queueAcknowledgements.push(ack);
				}
				//if the message is an acknowledgment sent by a Host to a Client
				else if(message.getMessageType().equals(Message.MessageType.acknowledgement)){
				    if(!this.isHost) {
				        System.out.println("Ack received for message by client: " +message.toString());
				        //remove the original message form the SentMessages queue
				        messageController.queueSentMessagesByClient.remove(message.getUsername(), Commands.getOriginalId(message));
				    } else { //a host can receive an ack from a client
				        removeResendMessageToClient(message);
				        
                        System.out.println("Ack received for message by host: " +message.toString());				        
				        
				    }
				}
				
				Thread.sleep(250);
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag = false;
			}
    	}
    }
    
    /**
     * Remove the client for a message as the client has sent an acknowledgement.
     * @param message
     */
    private void removeResendMessageToClient(Message message) {
        for(ForwardMessage forwardMessage: messageController.queueSentMessagesByHostToClient) {
//            System.out.println("Never here???? " + forwardMessage.getId());
            if(forwardMessage.getId() == Commands.getOriginalId(message)) { //correct message
                System.out.println("@@ OneToOneListener (host): Removing client as I received an ack.");
                forwardMessage.removeClient(message.getProcessID());
                break;
            }
        }
    }
    private void addNewClient(String processID, String username) {
    	if(!ConnectedClientsList.clientExists(processID)) {
	    	ConnectedClient newclient = new ConnectedClient(processID, username);
	        ConnectedClientsList.addClient(newclient); 
	        HostsList.updateHost(Misc.processID, ConnectedClientsList.size(), Config.master);
    	}
    }
}