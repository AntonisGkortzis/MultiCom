package sharedresources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public  class HostsList {
    private static List<Host> hosts = new ArrayList<>();
    
    public HostsList(){}
    
    /**
     * Add a new host to the list
     * @param host
     */
	public static void addHost(Host host){
        hosts.add(host);
//        System.out.println("adding host: " + host.toString()+ "\n\tnew size: " + hosts.size());
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
            }
        }
        
        return suitableHost;
    }
    
    public static Host getTheMostVotedHost(){
    	int max = -1;
        Host mostVotedHost = null;
        
        for(Host host : hosts){
        	System.out.println("@@@@ pid: " +host.getProcessID() +", port: "+host.getPort()+ ", nofClients: " 
					+ host.getNrOfClients() +", Votes: " + host.getNrOfVotes());
            if(host.getNrOfVotes()>max) {
                mostVotedHost = host;
            }
        }
        return mostVotedHost;
    }
    
    public static int size() {
        return hosts.size();
    }
    
    public static List<Host> getHostsList(){
    	return hosts;
    }

	public static void updateHostVote(String processID) {
		for(Host host: hosts) {
            if(host.getProcessID().equals(processID)) {
//            	System.out.println("\t\tvote for " + processID + " counted. ");
                host.setNrOfVotes(host.getNrOfVotes()+1);
            }
        }
		
	}

	public static void setMasterAndResetVotes(String processID) {
		//Set yourself as a master only if the processID of the voted is yours
		//otherwise set to false
		Config.master = processID.equals(Misc.processID);
		
		//Find the voted host and set his Master variable to True, False for the others
		for(Host host: hosts) {
			host.setNrOfVotes(0);
            if(host.getProcessID().equals(processID)) {
                host.setMaster(true);
                System.out.println("##-- Master is host with processId: "+ processID +" and port: " + host.getPort() + " --##");
            } else {
            	host.setMaster(false);
            }
        }
		
	}
	
	public static void printHostsVotes(){
		for(Host host: hosts) {
			System.out.println("\tpid: " +host.getProcessID() +", port: "+host.getPort()+ ", nofClients: " 
					+ host.getNrOfClients() +", Votes: " + host.getNrOfVotes());
		}
	}
	
	public static void resetVotes(){
		System.out.println("---> Resetting votes <----");
		for(Host host: hosts) {
			host.setNrOfVotes(0);
		}
	}
}
