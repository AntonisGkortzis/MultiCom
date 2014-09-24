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
    	boolean flag=true;
    	while(flag){
    		try {
    			ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
    			Message message = (Message)inStream.readObject();
			    Server.getQueue().push(message);
			    System.out.println("Server received: "+message.getText());
    		} catch (IOException | ClassNotFoundException ioe) {
    			System.out.println("IOException on socket listen: " + ioe);
    			ioe.printStackTrace();
    			flag=false;
    		}
    	}
    }
}