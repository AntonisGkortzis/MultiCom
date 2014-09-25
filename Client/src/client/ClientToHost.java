package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import sharedresources.Message;

/**
 * Used for sending one to one information to one specific host.
 * This information is of the types:
 *  - Chat messages to distributed by the host
 *  - Metadata information
 */

public class ClientToHost implements Runnable {
	
	private Socket clientSocket;
	private Client client;
	
	public ClientToHost(Client client) {
		try {
			this.client = client;
			this.clientSocket = new Socket(client.getHostName(), client.getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		    String hostname = InetAddress.getLocalHost().getHostName();
		    Message message = new Message(false, false, false, false,"processID;"+hostname, "username;"+hostname, client.getTextFromMainPanel());
		    objectWriter.writeObject(message);
		    
		 	System.out.println("hostname: " + hostname); //DEBUG
		    
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
