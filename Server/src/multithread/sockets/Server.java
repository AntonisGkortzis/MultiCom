package multithread.sockets;
import sharedresources.Message;
import sharedresources.MessageController;
import sharedresources.MessageQueue;
import sharedresources.OneToOneListener;


public class Server {


    public static boolean isMaster = false; // TODO Must be set by election process
    public static int port;
    //private static MessageQueue queue = new MessageQueue();
    private static MessageController messageController = null;

    // Listen for incoming connections and handle them
    public static void main(String[] args) {
        System.out.println("Server Running...");
        messageController = new MessageController();
        
        //start the thread for host discovery if this is the master
        if (isMaster) hostFinder(); //TODO change to Multicast broadcast and then listen
        

        //One to one communication between host and client
        HostToClient hostToClient = new HostToClient();
        hostToClient.start();       
        
        //One to many communication between host and its clients
        HostToMClient hostToMClient = new HostToMClient();
        hostToMClient.start();

        HostToMHost hostToMHost = new HostToMHost();
        hostToMHost.start();
    }

    /**
     * Host discovery
     */
    private static void hostFinder() {
        Thread t = new HostFinder();
        t.start();
    }    
   
}