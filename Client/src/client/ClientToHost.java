package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;
import sharedresources.Misc;

/**
 * Used for sending one to one information to one specific host.
 * This information is of the types:
 *  - Chat messages to distributed by the host
 *  - Metadata information
 *  - Send ping to check Host status 
 */

public class ClientToHost {
	
	private Socket clientSocket;
	private Client client;
	
	public ClientToHost(Client client) {
		try {
			this.client = client;
			this.clientSocket = new Socket(Config.hostName, Config.connectToPortFromHost);
            this.client.setServerStatus("Connection Established on port: " + Config.connectToPortFromHost,true); 
		} catch (IOException e) {
			client.showErrorMessage("Failed to make a connection to '"+Config.hostName+"' on port "+Config.connectToPortFromHost);
		}
	}
	
	public Socket getSocket(){
		return this.clientSocket;
	}
	
	public void sendMessage(String text) {
            Message message = new Message(Message.MessageType.hostChat, false, client.getUserName(), text, Misc.getNextMessageId());
            sendMessage(message);
            
            //after sending the message we should store it at the SentMessages queue and wait for its acknowledgment
            //initialize command is not acknowledged
            if(!Commands.messageIsOfCommand(message, Commands.initOneToOneWithHost))
            	Client.messageController.queueSentMessagesByClient.push(message);
	}
	
	public boolean sendMessage(Message message){
		if (clientSocket == null) {
			client.showErrorMessage("You are not connected.");
			return false;
		}
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			outputStream.writeObject(message);
			outputStream.flush();
			System.out.println("@@ Client to Host--> send message: " + message.getText() + " id: " + message.getId());
		} catch(IOException ex) {
			client.showErrorMessage("Connection closed, is the server running?\n"+ex.getMessage());
			client.closeSocket();
			client.setSocket(null);
//            ex.printStackTrace();
			return false;
		}
		return true;
	}

}
