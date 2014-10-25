package multithread.sockets;

import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.ConnectedClientsList;
import sharedresources.Message;
import sharedresources.Misc.MessageType;

public class SendStatusUpdate implements Runnable {
	public SendStatusUpdate() {
		
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
	    while(true) {
            String command = Commands.constructCommand(Commands.requestStatusUpdate, constructStatus());

          	Message message = new Message(MessageType.mHostStatus,true, command); 
          	Server.messageController.queueSend.push(message);
            try {
//                Thread.sleep(Config.DELAY);
                Thread.sleep(5000);
            } 
            catch (InterruptedException e) { 
                e.printStackTrace();
            }
		}
	}
	
	private static String constructStatus() {
	    return Commands.constructStatus(ConnectedClientsList.size(), Server.address, Server.port, Config.master);
	}
	
	public static Message createStatusMessage() {
        String command = Commands.constructCommand(Commands.statusUpdate, constructStatus()); 
		return new Message(MessageType.mHostStatus,true,command);
	}
}
