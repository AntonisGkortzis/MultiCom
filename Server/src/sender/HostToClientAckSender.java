package sender;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import multithread.sockets.Server;
import sharedresources.Message;

public class HostToClientAckSender implements Runnable {
	
	public HostToClientAckSender() {
	}
	
	@Override
	public void run() {
		boolean flag = true;
		while(flag){
			Message message = Server.messageController.queueAcknowledgements.pop();
			if(message != null ){
//				System.out.println("Sending acknowledgment " + message.toString());
				Socket socket = message.getSocket();
				message.setSocket(null);
				flag = sendMessage(socket, message);
			}	
			
			try {
				Thread.sleep(150); //TODO put delay in config. Must be faster than push from ping for now
			} 
			catch (InterruptedException e) { 
				e.printStackTrace();
				flag=false;
			}
			
		}

	}
	
	public void start(){
		Thread t = new Thread(this);
		t.start();
	}
	
	private boolean sendMessage(Socket socket, Message message) {
    	if (socket == null) {
			System.out.println("@AckSender Not connected to client");
			return false;
		}
    	try {
    		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
    		outputStream.writeObject(message);
    		outputStream.flush();
    		System.out.println("@@ AckSender--> send message: " + message.getText());
    	} catch(IOException ex) {
            ex.printStackTrace();
            return false;
    	}
    	
    	return true;
    }

}
