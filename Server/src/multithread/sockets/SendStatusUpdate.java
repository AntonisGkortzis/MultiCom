package multithread.sockets;

import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;
import sharedresources.Misc;
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

          	Message message = new Message(MessageType.mHostStatus,true,Misc.getProcessID(), null, command); 
          	Server.messageController.queueMHostsCommand.push(message);
            try {
                Thread.sleep(Config.DELAY);
            } 
            catch (InterruptedException e) { 
                e.printStackTrace();
            }
		}
	}
	
	private static String constructStatus() {
	    return Commands.constructStatus(Server.clients.size(), Server.address, Server.port, Config.master, Misc.getProcessID());
	}
	
	public static Message getStatusMessage() {
        String command = Commands.constructCommand(Commands.requestStatusUpdate, constructStatus()); 
		return new Message(MessageType.mHostStatus,true,Misc.getProcessID(), null, command);
//	    return Commands.constructStatus(Server.clients.size(), Server.address, Server.port, Config.master, Misc.getProcessID());
	}
}
