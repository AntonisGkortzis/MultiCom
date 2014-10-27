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
	private ServerSocket serverSocket;
	
	public static long messageId = 0;
    
    public OneToOneListener(ServerSocket serverSocket, MessageController messageController) {
    	this.serverSocket = serverSocket;
        this.messageController = messageController;
    }

    public void start() {
		t = new Thread(this);
		t.start();
    }
    
    public void run () {
    	ObjectInputStream inStream;
    	Message message;
    	boolean flag = true;
		
    	//Accepts a connection. This blocks until a connection is accepted
    	try {
			this.socket = this.serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	while(flag) {
	    	try {
				inStream = new ObjectInputStream(socket.getInputStream());
				message = (Message)inStream.readObject();
				System.out.println(message.toString());
				if(Commands.messageIsOfCommand(message, Commands.initOneToOneWithHost)) {
				    addNewClient(message.getProcessID(), message.getUsername());
				    //Not further action is needed as this is only a 
				    //message to let the host know that there is a new client.
				    //continue;
				}
				//if the message is a chat message (sent from client to host)
				else if(Commands.messageIsOfCommand(message, Commands.chatMessage)){
				    message.setProcessId(Misc.processID);
				    messageController.queueHostChat.push(message); //to send it to the clients connected on this host
				    String command = Commands.constructCommand(Commands.forwardMessage, message.getText());
				    Message newMessage = new Message(Message.MessageType.mHostChat,true, message.getUsername(), command, messageId);
				    messageController.queueSend.push(newMessage); //Send to other hosts
				    
				    //create the acknowledgement and store it in the queueAcknowledgments
				    command = Commands.constructCommand(Commands.acknowledgement, Long.toString(message.getId()));
				    Message ack = new Message(Message.MessageType.acknowledgement, true, command);
				    messageController.queueAcknowledgements.push(ack);
				}
				//if the message is an acknowledgment sent by a Host to a Client
				else if(Commands.messageIsOfCommand(message, Commands.acknowledgement)){
					System.out.println("Ack received for message: " +message.toString());
					//remove the original message form the SentMessages queue
					messageController.queueSentMessages.remove(message.getUsername(), Commands.getOriginalId(message));
				}
				
				Thread.sleep(250);
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag = false;
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