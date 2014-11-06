package sharedresources;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public  class HostsList {
    private static BlockingQueue<Host> hosts = new LinkedBlockingQueue<>();
    public final static int declareDead = 5000; //MilliSeconds
    
    public HostsList(){}
    
    /**
     * Add a new host to the list
     * @param host
     */
	public static void addHost(Host host){
	    host.setLastUpdate(new Date());
        hosts.add(host);
    }
    
    /**
     * Check if the host is known
     * @param host
     * @return
     */
    public static boolean hostExists(Host otherHost){
        for(Host host : hosts){
            if(host.getProcessID().equals(otherHost.getProcessID())){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Update the information of a host
     * Only changes number of clients and isMaster and the time of the last update
     * @param host
     */
    public static void updateHost(Host otherHost) {
        for(Host host: hosts) {
            if(host.getProcessID().equals(otherHost.getProcessID())) {
                update(host, otherHost.getNrOfClients(), otherHost.isMaster());
            }
        }
    }
    public static void updateHost(String processID, int nrOfClients, boolean isMaster) {
        for(Host host: hosts) {
            if(host.getProcessID().equals(processID)) {
                update(host, nrOfClients, isMaster);
            }
        }
    }
    
    private static void update(Host host, int nrOfClients, boolean isMaster) {
    	host.setNrOfClients(nrOfClients);
    	host.setMaster(isMaster);
    	host.setLastUpdate(new Date());
    }
    
    public static void printHostAddresses(int port){
        System.out.println("-- Hosts -- I am: " + port);
        for(Host host : hosts){
            System.out.println("\t" + host.getAddress() + " : " + host.getPort() + " clients: " + host.getNrOfClients() + " last updated: " + host.getLastUpdate());
        }
    }
    
    /**
     * Find the most suitable host.
     * This host has the least number of clients
     * @return
     */
    public static Host findSuitableHost() {
        int minimumClients = Integer.MAX_VALUE;
        Host suitableHost = null;
        for(Host host : hosts){
            if(host.getNrOfClients()<minimumClients) {
                suitableHost = host;
                minimumClients = host.getNrOfClients();
            }
        }
        
        return suitableHost;
    }
    
    public static Host getTheMostVotedHost(){
    	int max = -1;
        Host mostVotedHost = null;
        
        for(Host host : hosts){
            if(host.getNrOfVotes()>max) {
                mostVotedHost = host;
                max = host.getNrOfVotes();
            }
        }
        return mostVotedHost;
    }
    
    public static int size() {
        return hosts.size();
    }
    
    public static BlockingQueue<Host> getHostsList(){
    	return hosts;
    }

	public static void updateHostVote(String processID) {
		for(Host host: hosts) {
            if(host.getProcessID().equals(processID)) {
                host.setNrOfVotes(host.getNrOfVotes()+1);
            }
        }
	}

	public static void setMasterAndResetVotes(String processID) {
		//Set yourself as a master only if the processID of the chosen one is yours
		//otherwise set to false
		Config.master = processID.equals(Misc.processID);
		
		//Find the voted host and set his Master variable to True, False for the others
		for(Host host: hosts) {
			host.setNrOfVotes(0);
            if(host.getProcessID().equals(processID)) {
                host.setMaster(true);
            } else {
            	host.setMaster(false);
            }
        }
		
	}
	
	/**
	 * Check for all hosts if one of them is a master
	 * @return
	 */
	public static boolean masterAlive() {
		for(Host host: hosts) {
			if(host.isMaster()) {
				return true;
			}
		}
		return false;
	}

	public static void printHostsVotes(){
		for(Host host: hosts) {
			System.out.println("\tHost: " + host.getProcessID() +"/"+host.getPort()+ ", #Messengers: " 
					+ host.getNrOfClients() +", #Votes: " + host.getNrOfVotes());
		}
	}

	/**
	 * Finds hosts that are not responding anymore by checking their last update time
	 */
	public static void findDeadHosts() {
		long currentTime = new Date().getTime();
		
		Iterator<Host> itererator = hosts.iterator();
		Host host = null;
		while (itererator.hasNext()) { //Iterator used so you can remove an item during iteration
		    host = itererator.next();
		    
		    //skip yourself, you don't want to remove yourself, do ya?
		    if(host.getProcessID().equals(Misc.processID)) {
		    	continue;
		    }
		    long lastUpdateOfHost = host.getLastUpdate().getTime();
		    if(currentTime-lastUpdateOfHost > declareDead) {
		    	System.out.println("@@-- Removing a dead Host "+ host.getProcessID() + "/" + host.getPort() + " --@@");
		    	itererator.remove();		    	
		    }
		}
	}

	/**
	 * Counts the number of participants in this election by looking at the hosts' last update time
	 * @param electionStart The time when the election started
	 * @return the number of participants in this election
	 */
    public static int nrOfElectionParticipants(Date electionStart) {
        int nrOfParticipants = 0;
        for(Host host: hosts) {
            if(host.getProcessID().equals(Misc.processID)) { // because we do not update own host's time
                nrOfParticipants++;
            } else if(!host.getLastUpdate().before(electionStart)) {
                nrOfParticipants++;
            }
        }
        return nrOfParticipants;
    }

    public static Host getHost(String processId) {
        for(Host host: hosts) {
            if(host.getProcessID().equals(processId)) {
                return host;
            }
        }
        return null;
    }
    
    public static void addHoldBackMessage(Message message) {
        for(Host host: hosts) {
            if(host.getProcessID().equals(message.getProcessID())) {
                try {
                    host.holdbackQueue.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
