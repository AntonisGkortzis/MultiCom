package multithread.sockets;

import java.io.*;
import java.net.*;
import java.security.*;

/**
 * Title:        Sample Server
 * Description:  This utility will accept input from a socket, posting back to the socket before closing the link.
 * It is intended as a template for coders to base servers on. Please report bugs to brad at kieser.net
 * Copyright:    Copyright (c) 2002
 * Company:      Kieser.net
 * @author B. Kieser
 * @version 1.0
 */

public class SampleServer {

  private static int port=4444, maxConnections=10;
  // Listen for incoming connections and handle them
  public static void main(String[] args) {
    int i=0;
    System.out.println("SampleServer Running...");
    
    try{
      ServerSocket listener = new ServerSocket(port);
      Socket server;

      while((i++ < maxConnections) || (maxConnections == 0)){
        doComms connection;

        server = listener.accept();
        doComms conn_c= new doComms(server);
        Thread t = new Thread(conn_c);
        System.out.println("\tIncoming connection accepted. [connection: " + i + "]");
        t.start();
      }
    } catch (IOException ioe) {
      System.out.println("IOException on socket listen: " + ioe);
      ioe.printStackTrace();
    }
  }

}