package sharedresources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class OneToOneListener implements Runnable {
    private Socket server;

    public OneToOneListener(Socket server) {
      this.server=server;
    }

    public void run () {
    	boolean flag=true;
    	while(flag){
    		try {
    			ObjectInputStream inStream = new ObjectInputStream(server.getInputStream());
    			Message message = (Message)inStream.readObject();
			    Server.getQueue().push(message); // TODO import the Class..
			    System.out.println("Server received: "+message.getText());
    		} catch (IOException | ClassNotFoundException ioe) {
    			System.out.println("IOException on socket listen: " + ioe);
    			ioe.printStackTrace();
    			flag=false;
    		}
    	}
    }
}