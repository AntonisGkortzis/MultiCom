package monitor;

import multithread.sockets.Election;
import multithread.sockets.Server;
import sharedresources.HostsList;

public class StatusMonitor implements Runnable {

	Thread t;
	
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
				//Remove hosts that are found out to be dead
				HostsList.findDeadHosts();
				
                //Check if there is a master alive, if not start the elections!
                if(!HostsList.masterAlive()) {
                	//OH NO, there is no master alive. Let's elect:
                	if(Server.electionState.equals(Server.ElectionStates.normal)) {
                		System.out.println("@@-- Status monitor detected that no master is present, I will start elections --@@");
                		Election.initElection();
                	}
                }
			} catch (InterruptedException e) {
				e.printStackTrace();
				flag = false;
			}
		}
		
		
	}

}
