package multithread.sockets;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static int maxConnections = 10; // TODO set port by
                                                         // selecting free port
                                                         // in range
                                                         // [START_PORT,
                                                         // START_PORT+PORT_AMOUNT)
    private static boolean isMaster = true; // TODO Must be set by election
                                            // process
    private static List<doComms> connections = new ArrayList<doComms>();
    private static ConnectClientsList clients = new ConnectClientsList();

    // Listen for incoming connections and handle them
    public static void main(String[] args) {
        int i = 0;
        System.out.println("Server Running...");

        //start the thread for host discovery if this is the master
        if (isMaster) hostFinder();
        
        ServerSocket listener = null;
        boolean success = false;
        for(i = HostFinder.START_PORT; i<HostFinder.START_PORT + HostFinder.PORT_AMOUNT; i++ ) {
            try {
                listener = new ServerSocket(i);
            } catch (IOException ioe) {
                continue;
            }
            success = true;
            break;
        }
        if(!success) {
            System.out.println("No hosts available, exiting now...");
        } else {
            System.out.println("Listening for clients on port: " + i);
            listen(listener);
        }
    }

    /**
     * Host discovery
     */
    private static void hostFinder() {
        Thread t = new HostFinder();
        t.start();
    }
    
    /**
     * Listen for clients
     * @param listener the server socket
     */
    private static void listen(ServerSocket listener) {
        Socket server;
        int i=0;
        while ((i++ < maxConnections) || (maxConnections == 0)) {
            //doComms connection;
            try {
                server = listener.accept();
                System.out
                        .println("\tIncoming connection accepted. [connection: "
                                + i + "]");
                ConnectedClient newclient = new ConnectedClient(server, server
                        .getInetAddress().toString(), "client name"); // angor
                clients.addClient(newclient); // angor
                System.out.println("\tClient added to list. [connection: " + i
                        + "]"); // angor
                // doComms conn_c= new doComms(server); //angor
                doComms conn_c = new doComms(server, clients); // angor
                Thread t = new Thread(conn_c);
                t.start();
    
                if (isMaster) { // this host is the master host
                    connections.add(conn_c);
    
                    /* A new connection is added now notify other hosts */
    
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}