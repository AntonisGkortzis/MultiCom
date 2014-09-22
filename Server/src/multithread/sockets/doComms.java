package multithread.sockets;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;

import sharedresources.Message;

class doComms implements Runnable {
    private Socket server;
    private String line,input;
    private ConnectClientsList clients; //angor

    doComms(Socket server) {
      this.server=server;
    }
    
    doComms(Socket server, ConnectClientsList clients) { //angor
        this.server=server;
        this.clients = clients; //angor
      }

    public void run () {

      input="";

      try {
    	ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
    	Message message = (Message)inStream.readObject();
	    Server.getQueue().push(message);
	    System.out.println("Server received: "+message.getText());
        
	    
	    // Get input from the client
    	/*
        DataInputStream in = new DataInputStream (server.getInputStream());
        //PrintStream out = new PrintStream(server.getOutputStream());
        System.out.println("\tServer: new message received!");
        while((line = in.readLine()) != null && !line.equals(".")) {
          //input=input + line; //angor
          //out.println("Server: I got:" + line);
          System.out.println("\tI got:" + line);
          clients.updateClients(line); //angor
          //clients.printClientAddresses(); //angor
        }

        // Now write to the client

        System.out.println("Overall message is:" + input);
        //out.println("Server: Overall message is:" + input);

        server.close();
        System.out.println("...server terminated!");
        */
      } catch (IOException | ClassNotFoundException ioe) {
        System.out.println("IOException on socket listen: " + ioe);
        ioe.printStackTrace();
      }
    }
}