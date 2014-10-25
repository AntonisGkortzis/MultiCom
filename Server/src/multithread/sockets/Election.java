package multithread.sockets;

import java.util.Date;

import multithread.sockets.Server.ElectionStates;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.ConnectedClientsList;
import sharedresources.Host;
import sharedresources.HostsList;
import sharedresources.Message;
import sharedresources.MessageController;
import sharedresources.Misc;
import sharedresources.Misc.MessageType;

public class Election implements Runnable {

    private MessageController messageController;
    private Date electionStart;
    Thread t;
    
    public Election(MessageController messageController) {
        this.messageController = messageController;
        this.electionStart = new Date();
        
    }
    
    public void start() {
        t = new Thread(this);
        t.start();
    }
    
    public void stop(){
    	Server.electionState = ElectionStates.normal; 
    	t.interrupt();
//    	t.stop();
    }
    
    
    public void startElection() {
    	
    	try {
    		    		
    		Thread.sleep(2000);
    		System.out.println("##-- Host: " + Server.port + " starts participating in the Master's election [candidates: "+HostsList.size()+"] --##");
    		
    		//STEP 1a
    		// Go into the voting state
    		Server.electionState = ElectionStates.voting;
    		
    		//STEP 1b
    		// When elections are started you have to send your status update
    		String command = Commands.constructCommand(Commands.statusUpdate, 
    				Commands.constructStatus(ConnectedClientsList.size(), Server.address, Server.port, Config.master));
    		Message message = new Message(MessageType.mHostStatus, true, command);
    		messageController.queueSend.push(message);
    		
    		System.out.println("##-- Participants: " + HostsList.size() + " --##");
    		
    		//STEP 2
    		//Sleep & Wait to receive as many status updates as possible
            Thread.sleep(2000); 
            
            if(HostsList.size()>1) {
	            this.voteOnPreferredHostAndSend();
	            
	            //Wait to receive as many votes as possible
	            Thread.sleep(3000);
	            
	            this.electionResults();
            } else { //this host is the only host so you are the master
            	System.out.println("##-- Only one host, so I took the liberty of announcing myself Master --##");
            	Config.master = true;
            	HostsList.setMasterAndResetVotes(Misc.processID);
            	Server.electionState = ElectionStates.normal;
            }
    		System.out.println("##-- Host: " + Server.port + " exits the election. --##");

                        
    	} catch (InterruptedException e) {
            // TODO Auto-generated catch block
    		
            e.printStackTrace();
            Server.electionState = ElectionStates.normal;
            return;
        }
    	System.out.println("--- HostList of host " + Misc.processID);
    	HostsList.printHostsVotes();

    }
    
    
    /**
     * Vote on a preferred candidate and send this vote through global multicast
     */
    private void voteOnPreferredHostAndSend() {
    	//STEP 3a
        //get the most suitable candidate
        Host preferredCandidate = getPreferredCandidate();
        
        //STEP 3b
        //Creating adding (for sending) the voting message
        String command = Commands.constructCommand(Commands.vote, preferredCandidate.getProcessID());
        Message vote = new Message(MessageType.mHostVote, true, command);
        messageController.queueSend.push(vote);
        System.out.println("##-- I ["+Misc.processID+","+Server.port + "] vote for Host [" 
        		+ preferredCandidate.getProcessID() +","+ preferredCandidate.getPort() +"] --##");
        
        //STEP 3c
        //Go into the voted state
        Server.electionState = Server.ElectionStates.voted; 
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
        System.out.println("--> My ["+Misc.processID+"] preferred candidate is "+preferredCandidate.getProcessID());
        return preferredCandidate;
    }
    
    /**
     * Find the host with the most votes and announce yourself the leader if 
     * you are chosen as the master.
     */
    private void electionResults() {
    	//STEP 4
        //Parse the Hosts list in order to find the one with the most votes.
        Host mostVotedHost  = HostsList.getTheMostVotedHost();
        //if YOU are the most voted [votes > 1/2list size] host then announce yourself as the Master
        if(Misc.processID.equals(mostVotedHost.getProcessID())){
        	String command = Commands.constructCommand(Commands.IAmTheMaster);
        	Message master = new Message(MessageType.mHostCommand, true, command);
        	messageController.queueSend.push(master);
        }
    }
    @Override
    public void run() {
        startElection();        
    }

}
