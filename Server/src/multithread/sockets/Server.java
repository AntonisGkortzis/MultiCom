package multithread.sockets;
import java.util.Date;

import sharedresources.ConnectedClientsList;
import sharedresources.Host;
import sharedresources.HostsList;
import sharedresources.Config;
import sharedresources.MessageController;
import sharedresources.Misc;
import sharedresources.OneToManyListener;

/**
 * This class is used to start a server and initiates all the connections.
 * The types of connections created are:
 *  - Host to client communication
 *  - Host to multiple clients communication
 *  - Host to multiple hosts communication
 *
 */
public class Server {

    public static int port; //port for one to one connections with clients
    public static int mPort; // port for multicast message to clients
    
    public static String address;
    //private static MessageQueue queue = new MessageQueue();
    
    
    public static MessageController messageController = new MessageController();
//    public static MessageController messageControllerMHost = new MessageController();
//    public static MessageController messageControllerForStatusUpdates = new MessageController();
    
    
//    public static ConnectClientsList clients = new ConnectClientsList();

    public static enum ElectionStates {
    	normal, // when no election is taking place
    	voting, //upon receiving the "startElection" command
    	voted, // Once a vote is made
    	finished // The election is finished and a new leader is born
    }
    
    public static ElectionStates electionState = ElectionStates.normal;
    
    // Listen for incoming connections and handle them
    public static void main(String[] args) {
        System.out.println("Server Running with processID " + Misc.getProcessID() + " and port " + port + " ...");
        
        //start the thread for host discovery if this is the master
//        if (Config.master) hostFinder(); //TODO change to Multicast broadcast and then listen
        
        //Adding yourself in the AvailableHosts list

        
        //One to one communication between host and client
        HostToClient hostToClient = new HostToClient();
        hostToClient.start();       
        
        //One to many communication between host and its clients
        HostToMClient hostToMClient = new HostToMClient();
        hostToMClient.start();

        HostToMHost hostToMHost = new HostToMHost();
        hostToMHost.start();
        
        
        OneToManyListener oneToManyListener = new OneToManyListener(messageController, true);
        oneToManyListener.start();
    }

    /**
     * Host discovery
     */
//    @SuppressWarnings("unused")
//    private static void hostFinder() { //TODO remove and remove HostFinder()
//        Thread t = new HostFinder();
//        t.start();
//    }    
   
}