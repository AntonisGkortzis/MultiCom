package monitor;

import multithread.sockets.Election;
import sharedresources.HostsList;

public class StatusMonitor implements Runnable {

	Thread t;
	//TODO in REPORT
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
				//Remove hosts that are found out to be dead
				HostsList.findDeadHosts();
				
//				HostsList.printHostsVotes(); //DEBUG
                //Check if there is a master alive, if not start the elections!
                if(!HostsList.masterAlive()) {
                	System.out.println("##-- No master alive, I will start elections --#");
                	//OH NO, there is no master alive. Let's elect:
                	Election.initElection();
                }
			} catch (InterruptedException e) {
				e.printStackTrace();
				flag = false;
			}
		}
		
		
	}

}
