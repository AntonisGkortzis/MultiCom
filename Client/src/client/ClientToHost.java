package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import sharedresources.Config;
import sharedresources.Message;
import sharedresources.Misc;
import sharedresources.Misc.MessageType;

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

//	public void start() {
//        if(clientSocket != null) {
//            Thread t = new Thread(this);
//            t.start();
//        }
//    }

//	@Override
//	public void run() { //TODO Test if this is used? If not put code from Client.java SendMessageButtonActionPerformed in here
//		// TODO Auto-generated method stub
//		try {
//		    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//		    //BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
//		    ObjectOutputStream objectWriter = new ObjectOutputStream(clientSocket.getOutputStream());
//		    String serverMsg;
//		    Message message = new Message(MessageType.hostChat, false,Misc.getProcessID(), client.getUserName(), client.getTextFromMainPanel());
//		    objectWriter.writeObject(message);
//		    		    
//		 	System.out.println("hostname: " + InetAddress.getLocalHost().getHostName()); //DEBUG
//		    
//            while ((serverMsg = reader.readLine()) != null) {
//            	client.AddTextToMainPanel(serverMsg);
//            }
//            
//        } catch(Exception e) {
//            e.printStackTrace();
//            System.out.println("Client: "+"Connection failed..");
//            client.setServerStatus("Connection failed..", false);
//        }
//	}
	public void sendMessage(String text) {
		if (clientSocket == null) {
            client.showErrorMessage("You are not connected.");
            return;
    	}
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            Message message = new Message(MessageType.hostChat, false, Misc.getProcessID(), client.getUserName(), text);
            outputStream.writeObject(message);
            outputStream.flush();
            System.out.println("@@ Client to Host--> send message: " + message.getText());
        } catch(IOException ex) {
            client.showErrorMessage("Connection closed, is the server running?\n"+ex.getMessage());
            client.closeSocket();
//            ex.printStackTrace();
        }
		
	}

}
