package multithread.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import sharedresources.Message;

public class HostToClient implements Runnable{

    private static int maxConnections = 10;
    private static List<doComms> connections = new ArrayList<doComms>();
    private static ConnectClientsList clients = new ConnectClientsList();
    private ServerSocket listener;
    
    public HostToClient() {
        int i = 0;
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
            Server.port = i;
            System.out.println("Listening for clients on port: " + i);
        }
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
                ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
                Message message = new Message();
                message = (Message)inStream.readObject();
                if(message.isCommand()){
                    System.out.println("Command: " +message.getText());
                }
                
                System.out.println("\tClient added to list. [connection: " + i
                        + "]"); // angor
                // doComms conn_c= new doComms(server); //angor
                // TODO remove
                doComms conn_c = new doComms(server, clients); // angor
                Thread t = new Thread(conn_c);
                t.start();
    
//                if (isMaster) { // this host is the master host
                connections.add(conn_c);
                // TODO end remove
    
            } catch(IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        listen(listener);
        
    }

}
