package multithread.sockets;

import java.util.Date;

import sharedresources.Commands;
import sharedresources.Host;
import sharedresources.HostsList;
import sharedresources.Message;
import sharedresources.Misc;
import sharedresources.Misc.MessageType;

public class Election implements Runnable {

    private HostToMHost hostToMHost;
    private Date electionStart;
    public Election(HostToMHost hostToMHost) {
        this.hostToMHost = hostToMHost;
        
        Server.electionState = Server.ElectionStates.voting; // Go into the voting state
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    
    public void election() {
    
  
    }
    
    public void startElection() {
    	try {
    		String command = Commands.constructCommand(Commands.startElection);
    		Message message = new Message(MessageType.mHostVote, true, Misc.getProcessID(), command );
    		hostToMHost.sendMessage(message);
    		//Wait to receive as many status updates as possible
            Thread.sleep(5000); 
            
            //get the most suitable candidate
            Host preferredCandidate = getPreferredCandidate();
            //Create the voting message
            command = Commands.constructCommand(Commands.vote, preferredCandidate.getProcessID());
            Message vote = new Message(MessageType.mHostVote, true, Misc.getProcessID(),command);
            // Go into the voted state
            Server.electionState = Server.ElectionStates.voted; 
            //Wait to receive as many votes as possible
            Thread.sleep(3000);
            
            //TODO parse the Hosts list in order to find the one with the most vote. 
            //if YOU are the most voted [votes > 1/2list size] host then announce yourself as the Master
            //TODO keep a vote count variable in Host and update it when vote messages arrive
            
    	} catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    /**
     * Returns the host with the lowest number of connected clients from the HostsList 
     * or the one with the lowest port number if the number of clients is equal
     */
    public Host getPreferredCandidate(){
    	//initialize the preferred candidate and the minimum (his) number of connected clients
    	Host preferredCandidate = HostsList.getHostsList().get(0);
    	int leastClients = preferredCandidate.getNrOfClients();
        for(Host host : HostsList.getHostsList()){
        	//if the last update of the host is older than the election timestamp ignore him, because he is not part of the election
        	if(host.getLastUpdate().before(this.electionStart)){
        		continue;
        	}
        	if(host.getNrOfClients() < leastClients || (host.getNrOfClients() == leastClients 
        			&& host.getPort() < preferredCandidate.getPort())) {
        		preferredCandidate = host;
        	}
        }
        return preferredCandidate;
    }
    
    @Override
    public void run() {
        startElection();        
    }

}
