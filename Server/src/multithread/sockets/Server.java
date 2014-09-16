package multithread.sockets;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Title:        Sample Server
 * Description:  This utility will accept input from a socket, posting back to the socket before closing the link.
 * It is intended as a template for coders to base servers on. Please report bugs to brad at kieser.net
 * Copyright:    Copyright (c) 2002
 * Company:      Kieser.net
 * @author B. Kieser
 * @version 1.0
 */

public class Server {

  private static int port=4444, maxConnections=10; //TODO set port by selecting free port in range [START_PORT, START_PORT+PORT_AMOUNT)
  private static boolean isMaster = true; // TODO Must be set by election process
  private static List<doComms> connections = new ArrayList<doComms>();
  private static ConnectClientsList clients = new ConnectClientsList(); //angor
  // Listen for incoming connections and handle them
  public static void main(String[] args) {
    int i=0;
    System.out.println("Server Running...");
    
    if(isMaster) hostFinder();
    
    try{
      ServerSocket listener = new ServerSocket(port);
      Socket server;

      while((i++ < maxConnections) || (maxConnections == 0)){
        doComms connection;

        server = listener.accept();
        System.out.println("\tIncoming connection accepted. [connection: " + i + "]");
        ConnectedClient newclient = new ConnectedClient(server, server.getInetAddress().toString(), "client name"); //angor
        clients.addClient(newclient); //angor
        System.out.println("\tClient added to list. [connection: " + i + "]"); //angor
//        doComms conn_c= new doComms(server); //angor
        doComms conn_c= new doComms(server, clients); //angor
        Thread t = new Thread(conn_c);
        t.start();
        
        if(isMaster) { // this host is the master host
        	connections.add(conn_c);
        	
        	/* A new connection is added now notify other hosts*/
        	
        }
      }
    } catch (IOException ioe) {
      System.out.println("IOException on socket listen: " + ioe);
      ioe.printStackTrace();
    }
  }

  private static void hostFinder() {
	  Thread t = new HostFinder();
	  t.start();
  }
}