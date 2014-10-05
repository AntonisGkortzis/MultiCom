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
            String command = Commands.constructCommand(Commands.statusUpdate, constructStatus());

          	Message message = new Message(MessageType.multipleReceivers,true,Misc.getProcessID(), "host", command); 
          	Server.messageControllerMHost.push(message);
            try {
                Thread.sleep(Config.DELAY);
            } 
            catch (InterruptedException e) { 
                e.printStackTrace();
            }
		}
	}
	
	private String constructStatus() {
	    return Commands.constructStatus(Server.clients.size(), Server.address, Server.port, Config.master, Misc.getProcessID());
	}
}
