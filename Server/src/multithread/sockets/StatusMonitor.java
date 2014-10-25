package multithread.sockets;

import sharedresources.Commands;
import sharedresources.HostsList;
import sharedresources.Message;
import sharedresources.Misc.MessageType;

public class StatusMonitor implements Runnable {

	Thread t;
	
	public StatusMonitor() {
		
	}
	
	public void start() {
		t = new Thread(this);
		t.start();
	}
	@Override
	public void run() {
		boolean flag = true;
		
		while(flag) {
			try {
				//Must be put at the start of the algorithm
				Thread.sleep(HostsList.declareDead); //Make it milliseconds
				System.out.println("@@-- Monitor status --@@");
				//Remove hosts that are found out to be dead
				HostsList.findDeadHosts();
				
				HostsList.printHostsVotes();
                //Check if there is a master alive, if not start the elections!
                if(!HostsList.masterAlive()) {
                	System.out.println("##-- No master alive, I will start elections --#");
                	//OH NO, there is no master alive. Let's elect:
                    String command = Commands.constructCommand(Commands.startElection);
            		Message electionsStart = new Message(MessageType.mHostCommand, true, command);
            		Server.messageController.queueSend.push(electionsStart);
                }
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag = false;
			}
		}
		
		
	}

}
