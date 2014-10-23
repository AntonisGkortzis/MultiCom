package multithread.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
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
        Host newHost = new Host(ConnectedClientsList.size(), Server.address, Server.port, Config.master, Misc.getProcessID());
        newHost.setLastUpdate(new Date());
        HostsList.addHost(newHost);
        
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    
    public ServerSocket getSocket() {
    	return socket;
	
    }
    
    
    
//    /**
//     * Listen for clients
//     * @param listener the server socket
//     * @throws  
//     */
//    private void listen(ServerSocket listener)  {
//        OneToOneListener conn_c;
//		try {
//			conn_c = new OneToOneListener(listener.accept(), Server.messageController);
//			Thread t = new Thread(conn_c);
//			t.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}                 
//    }

    @Override
    public void run() {
//        listen(listener);
        
    }

}
