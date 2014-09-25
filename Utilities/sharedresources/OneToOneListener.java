package sharedresources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * This class is used for a listener of a one to one connection.
 * 
 */
public class OneToOneListener implements Runnable {
    private Socket server;

    public OneToOneListener(Socket server) {
      this.server=server;
    }

    public void run () {
    	ObjectInputStream inStream;
    	Message message;
    	boolean flag = true;
    	while(flag) {
	    	try {
				while(server.getInputStream().available()>0){
					try {
						inStream = new ObjectInputStream(server.getInputStream());
						message = (Message)inStream.readObject();
					    MessageController.push(message); // TODO import the Class..
					    System.out.println("Server received: "+message.getText());
					} catch (IOException | ClassNotFoundException ioe) {
						System.out.println("IOException on socket listen: " + ioe);
						ioe.printStackTrace();
						flag = false;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag = false;
			}
    	}
    }
}