package multithread.sockets;

import java.io.IOException;
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
        
        // Add yourselves to the list of hosts
        Host newHost = new Host(ConnectedClientsList.size(), Server.address, Server.port, Config.master);
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
				e.printStackTrace();
			}
    		OneToOneListener oneToOneListener = new OneToOneListener(socket, Server.messageController, true);
    		oneToOneListener.start();
            	
        }
        
        try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
}
