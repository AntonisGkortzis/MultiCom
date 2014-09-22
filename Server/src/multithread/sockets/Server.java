package multithread.sockets;


public class Server {


    public static boolean isMaster = false; // TODO Must be set by election process
    public static int port;

    // Listen for incoming connections and handle them
    public static void main(String[] args) {
        System.out.println("Server Running...");

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