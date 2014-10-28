package multithread.sockets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import sharedresources.*;

/**
 * This class is used for one to one communication between a host and a client. 
 * This communication is used for sending acknowledgments to clients that a message is delivered.
 *
 */
public class HostToClient implements Runnable{

    private ServerSocket serverSocket;
    
    public HostToClient() {
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Server.port = serverSocket.getLocalPort();
        Server.address = serverSocket.getInetAddress().getHostAddress();
        System.out.println("Listening for clients on port: " + Server.port);
        
        // Add yourselves to the list of hosts
        Host newHost = new Host(ConnectedClientsList.size(), Server.address, Server.port, Config.master);
//        newHost.setLastUpdate(new Date()); done in addhost
        HostsList.addHost(newHost);
        
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    
    public ServerSocket getSocket() {
    	return serverSocket;
	
    }

    @Override
    public void run() {
    	//sending acknowledgement for parsed messages.
    	boolean flag=true;
    	Socket socket = null;
        while (flag) {
        	//Accepts a connection. This blocks until a connection is accepted
    		try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		OneToOneListener oneToOneListener = new OneToOneListener(socket, Server.messageController);
    		oneToOneListener.start();
            	
//            //Messages that are only to be sent
//        	Message message = Server.messageController.queueAcknowledgements.pop();
//            if(message != null ){
//            	System.out.println("Sending acknowledgment " + message.toString());
//            	flag = this.sendMessage(socket, message);
//            }	
//    	
//            try {
//                Thread.sleep(150); //TODO put delay in config. Must be faster than push from ping for now
//            } 
//            catch (InterruptedException e) { 
//                e.printStackTrace();
//                flag=false;
//            }
        }
        
        try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

    private boolean sendMessage(Socket socket, Message message) {
    	if (socket == null) {
			System.out.println("@HostToClient Not connected to client");
			return false;
		}
    	try {
    		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
    		outputStream.writeObject(message);
    		outputStream.flush();
    		System.out.println("@@ HostToClient--> send message: " + message.getText());
    	} catch(IOException ex) {
            ex.printStackTrace();
            return false;
    	}
    	
    	return true;
    }
}
