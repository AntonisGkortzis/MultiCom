package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
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

public class ClientToHost implements Runnable {
	
	private Socket clientSocket;
	private Client client;
	
	public ClientToHost(Client client) {
		try {
			this.client = client;
			this.clientSocket = new Socket(Config.hostName, Config.connectToPortFromHost);
            this.client.setServerStatus("Connection Established!",true); 

		} catch (IOException e) {
			client.showErrorMessage("Failed to make a connection to '"+Config.hostName+"' on port "+Config.connectToPortFromHost);
		}
		
	}
	public Socket getSocket(){
		return this.clientSocket;
	}

	public void start() {
        if(clientSocket != null) {
            Thread t = new Thread(this);
            t.start();
        }
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
		    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    //BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
		    ObjectOutputStream objectWriter = new ObjectOutputStream(clientSocket.getOutputStream());
		    String serverMsg;
		    Message message = new Message(MessageType.hostAsReceiver, false,Misc.getProcessID(), client.getUserName(), client.getTextFromMainPanel());
		    objectWriter.writeObject(message);
		    		    
		 	System.out.println("hostname: " + InetAddress.getLocalHost().getHostName()); //DEBUG
		    
            while ((serverMsg = reader.readLine()) != null) {
            	client.AddTextToMainPanel(serverMsg);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Client: "+"Connection failed..");
            client.setServerStatus("Connection failed..", false);
        }
	}

}
