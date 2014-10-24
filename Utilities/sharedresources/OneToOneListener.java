package sharedresources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import sharedresources.Misc.MessageType;

/**
 * This class is used for a listener of a one to one connection.
 * 
 */
public class OneToOneListener implements Runnable {
    private Socket socket;
    private MessageController messageController;
    private Thread t;
	private ServerSocket serverSocket;
    
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
				
                addNewClient(message.getProcessID(), message.getUsername());
				
			    message.setProcessId(Misc.processID);
			    messageController.queueHostChat.push(message); //to send it to the clients connected on this host
			    String command = Commands.constructCommand(Commands.forwardMessage, message.getText());
			    Message newmessage = new Message(MessageType.mHostChat,true, message.getUsername(), command);
			    messageController.queueMHostsChat.push(newmessage); //to send it to other Hosts
			    System.out.println("Server received: "+message.getText());

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