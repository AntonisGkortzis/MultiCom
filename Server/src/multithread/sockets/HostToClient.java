package multithread.sockets;

import java.io.IOException;
import java.net.ServerSocket;

import sharedresources.*;

/**
 * This class is used for one to one communication between a host and a client. 
 * This communication is used for sending acknowledgments to clients that a message is delivered.
 *
 */
public class HostToClient implements Runnable{

    private ServerSocket socket;
    
    public HostToClient() {
        try {
            socket = new ServerSocket(0);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Server.port = socket.getLocalPort();
        Server.address = socket.getInetAddress().getHostAddress();
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
    	return socket;
	
    }

    @Override
    public void run() {
    	//sending acknowledgement for parsed messages.
    	boolean flag=true;
        while (flag) {
        	
            //Messages that are only to be sent
        	Message message = Server.messageController.queueAcknowledgements.pop();
            if(message != null ){
            	System.out.println("Sending acknowledgment " + message.toString());
            	flag = HostToMHost.sendMessage(message);
            }	
    	
            try {
                Thread.sleep(150); //TODO put delay in config. Must be faster than push from ping for now
            } 
            catch (InterruptedException e) { 
                e.printStackTrace();
                flag=false;
            }
        }
        
        try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

}
