package sender;

import multithread.sockets.Server;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.ConnectedClientsList;
import sharedresources.HostsList;
import sharedresources.Message;
import sharedresources.Misc;

public class SendStatusUpdate implements Runnable {

    public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
	    while(true) {
	        //update yourself before sending status update
	        HostsList.updateHost(Misc.processID, ConnectedClientsList.size(), Config.master);
            
//	        String command = Commands.constructCommand(Commands.requestStatusUpdate, constructStatus());
//          	Message message = new Message(Message.MessageType.mHostStatus, command); 
          	Message message = createStatusMessage();
          	
          	Server.messageController.queueSend.push(message);
            try {
                Thread.sleep(HostsList.declareDead/2); //must send faster or else you die while alive. And we don't like zombies
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
		return new Message(Message.MessageType.mHostStatus,command);
	}
}
