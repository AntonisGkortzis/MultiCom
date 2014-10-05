package sharedresources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * This class is used for a listener of a one to one connection.
 * 
 */
public class OneToOneListener implements Runnable {
    private Socket socket;
    private MessageController messageController;

    public OneToOneListener(Socket socket, MessageController messageController) {
        this.socket=socket;
        this.messageController = messageController;
    }

    public void run () {
    	ObjectInputStream inStream;
    	Message message;
    	boolean flag = true;
    	while(flag) {
	    	try {
				while(socket.getInputStream().available()>0){
					try {
						inStream = new ObjectInputStream(socket.getInputStream());
						message = (Message)inStream.readObject();
					    messageController.push(message);
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