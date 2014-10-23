package multithread.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//import sharedresources.Message;
import sharedresources.*;

/**
 * This class is used for one to one communication between a host and a client. 
 * This communication is used for sending acknowledgments to clients that a message is delivered.
 *
 */
public class HostToClient implements Runnable{

    private static int maxConnections = 10;
    private static List<OneToOneListener> connections = new ArrayList<OneToOneListener>();
    private ServerSocket listener;
    
    public HostToClient() {
        try {
            listener = new ServerSocket(0);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Server.port = listener.getLocalPort();
        Server.address = listener.getInetAddress().getHostAddress();
        System.out.println("Listening for clients on port: " + Server.port);
        
        // Add yourselves to the list of hosts
        Host newHost = new Host(ConnectedClientsList.size(), Server.address, Server.port, Config.master, Misc.getProcessID());
        newHost.setLastUpdate(new Date());
        HostsList.addHost(newHost);
        
    }
    
    public void start() {
        if(listener!=null) {
            Thread t = new Thread(this);
            t.start();
        }
    }
    /**
     * Listen for clients
     * @param listener the server socket
     * @throws  
     */
    private void listen(ServerSocket listener)  {
//        Socket server;
//        int i=0;
//        while ((i++ < maxConnections) || (maxConnections == 0)) {
        	// TODO decrease i when a client is disconnected
//            try {
//                server = listener.accept();
//                System.out.println("@HostToClient");
//                System.out.println("\tIncoming connection accepted. [connection: " + i + "]");
//                ConnectedClient newclient = new ConnectedClient(server, server.getInetAddress().toString(), "client name"); // angor
//                Server.clients.addClient(newclient); 
//                HostsList.updateHost(Misc.getProcessID(), Server.clients.size(), Config.master);
//                System.out.println("@@ I have how many clients? " + Server.clients.size());
//
//                while(server.getInputStream().available() > 0) {
//                	ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
//                	Message message = (Message)inStream.readObject(); //TODO why not used?
//                }
                //System.out.println("\tClient added to list. [connection: " + i + "]");  
                // TODO remove
            OneToOneListener conn_c;
			try {
				conn_c = new OneToOneListener(listener.accept(), Server.messageController);
				Thread t = new Thread(conn_c);
				t.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}                 
                
//                connections.add(conn_c);
                // TODO end remove
                
//            } catch(IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void run() {
        listen(listener);
        
    }

}
