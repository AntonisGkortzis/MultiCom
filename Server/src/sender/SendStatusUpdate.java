package sender;

import multithread.sockets.Server;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.ConnectedClientsList;
import sharedresources.HostsList;
import sharedresources.Message;
import sharedresources.Misc;

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
	        //update yourself before sending status update
	        HostsList.updateHost(Misc.processID, ConnectedClientsList.size(), Config.master);

            String command = Commands.constructCommand(Commands.requestStatusUpdate, constructStatus());

          	Message message = new Message(Message.MessageType.mHostStatus, command); 
          	Server.messageController.queueSend.push(message);
            try {
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
		return new Message(Message.MessageType.mHostStatus,command);
	}
}
