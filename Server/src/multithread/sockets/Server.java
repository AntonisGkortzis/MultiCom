package multithread.sockets;
import sharedresources.MessageController;
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
    public static MessageController messageControllerMClient = new MessageController();
    public static MessageController messageControllerMHost = new MessageController();
    public static MessageController messageControllerForStatusUpdates = new MessageController();
    public static ConnectClientsList clients = new ConnectClientsList();

    // Listen for incoming connections and handle them
    public static void main(String[] args) {
        System.out.println("Server Running...");
        
        //start the thread for host discovery if this is the master
//        if (Config.master) hostFinder(); //TODO change to Multicast broadcast and then listen
        
        
        //One to one communication between host and client
        HostToClient hostToClient = new HostToClient();
        hostToClient.start();       
        
        //One to many communication between host and its clients
        HostToMClient hostToMClient = new HostToMClient();
        hostToMClient.start();

        HostToMHost hostToMHost = new HostToMHost();
        hostToMHost.start();
        
        OneToManyListener oneToManyListener = new OneToManyListener(messageControllerMHost, messageControllerForStatusUpdates, true);
        oneToManyListener.start();
    }

    /**
     * Host discovery
     */
    @SuppressWarnings("unused")
    private static void hostFinder() { //TODO remove and remove HostFinder()
        Thread t = new HostFinder();
        t.start();
    }    
   
}