package multithread.sockets;

import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;
import sharedresources.Misc;
import sharedresources.Misc.MessageType;

public class SendPing implements Runnable {
	public SendPing() {
		
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
	    while(true) {
            String command;
            if(Config.master) {
              	command = Commands.masterPing;
            } else {
              	command = Commands.hostPing;
            }
          	command = Commands.constructCommand(command);
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
}
