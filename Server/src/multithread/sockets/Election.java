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

public class Election implements Runnable {

    private MessageController messageController;
    private Date electionStart; //To prevent votes from hosts participating too late. Their latest status update is too old
    Thread t;
    
    private String starterProcessID; //PID of the host starting the election
    private long starterElectionTime; //Time of the host starting the election at start of election
    
    public Election(MessageController messageController, String starterProcessID, long starterElectionTime) {
        this.messageController = messageController;
        this.electionStart = new Date();
        this.starterProcessID = starterProcessID;
        this.starterElectionTime = starterElectionTime;
        
    }

	public void start() {
        t = new Thread(this);
        t.start();
    }
    
    public void stop(){
    	Server.electionState = ElectionStates.normal; 
    	t.interrupt();
    }
    
    
    public void startElection() {
    	System.out.println();
    	System.out.println("######## Master Elections ########");
    	System.out.println("##-- Host " + Misc.processID + "/"+ Server.port 
    			+ " starts participating in the elections triggered by Host " + starterProcessID + " --##");
    	try {
    		while(!Thread.interrupted()) {	
	    		Thread.sleep(2000);
	    		
	    		//STEP 1a
	    		// Go into the voting state
	    		Server.electionState = ElectionStates.voting;
	    		
	    		//STEP 1b
	    		// When elections are started you have to send your status update
	    		String command = Commands.constructCommand(Commands.statusUpdate, 
	    				Commands.constructStatus(ConnectedClientsList.size(), Server.address, Server.port, Config.master));
	    		Message message = new Message(Message.MessageType.mHostStatus, command);
	    		messageController.queueSend.push(message);
	    		
	    		//STEP 2
	    		//Sleep & Wait to receive as many status updates as possible
	            Thread.sleep(2000); 
	            
	            System.out.println("##-- " + HostsList.nrOfElectionParticipants(electionStart) + " Host(s) participating in the Elections --##");
	            if(HostsList.nrOfElectionParticipants(electionStart)>1) {
		            this.voteOnPreferredHostAndSend();
		            
		            //Wait to receive as many votes as possible
		            Thread.sleep(3000);
		            
		            this.electionResults();
	            } else { //this host is the only host so you are the master
	            	System.out.println("@@-- Only one participant found. Declaring myself as the Master --@@");
	            	Config.master = true;
	            	HostsList.setMasterAndResetVotes(Misc.processID);
	            	Server.electionState = ElectionStates.normal;
	            }
	            break;
    		}
                        
    	} catch (InterruptedException e) {
//            e.printStackTrace();
    	    System.out.println("##-- Elections interrupted --##");
            Server.electionState = ElectionStates.normal;
            return;
        }
    	System.out.println("##-- Received votes from participants --##");
    	HostsList.printHostsVotes();
    	
    	if(HostsList.nrOfElectionParticipants(electionStart)<=1) {
        	System.out.println("##################################\n");
    	}
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
        String command = Commands.constructVoteCommand(Commands.vote, starterProcessID ,  starterElectionTime, preferredCandidate.getProcessID());
        Message vote = new Message(Message.MessageType.mHostVote, command);
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
    	Host preferredHost = HostsList.getHostsList().peek();
        for(Host host : HostsList.getHostsList()){
        	//if the last update of the host is older than the election timestamp ignore him, because he is not part of the election
        	if(host.getLastUpdate().before(this.electionStart)){
        		continue;
        	}
        	//host is chosen if it has less clients or
        	// if it has the same nr of clients and lower port
        	// if it has the same nr of clients and same ports the addresses are compared and lowest is picked
        	if(host.getNrOfClients() < preferredHost.getNrOfClients() || 
        	        (host.getNrOfClients() == preferredHost.getNrOfClients() 
        	        && host.getPort() < preferredHost.getPort())
        		|| (host.getNrOfClients()==preferredHost.getNrOfClients() 
        		        && host.getPort()==preferredHost.getPort() 
        		        && host.getAddress().compareTo(preferredHost.getAddress())<0)) {
        		preferredHost = host;
        	}
        }
        System.out.println("@@-- I vote for Host " + preferredHost.getProcessID() +"/" + preferredHost.getPort() + " --@@");
        return preferredHost;
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
            int nrOfParticipants = HostsList.nrOfElectionParticipants(this.electionStart);
            System.out.println("##-- In this election. #participants: " + nrOfParticipants + " #MaxVotes: " + mostVotedHost.getNrOfVotes() + " --##");
            if(mostVotedHost.getNrOfVotes() >  nrOfParticipants/2) { // majority
            	String command = Commands.constructElectionMessage(Commands.IAmTheMaster, this.starterProcessID, this.starterElectionTime);
            	Message master = new Message(Message.MessageType.mHostCommand, command);
            	messageController.queueSend.push(master);
            } else { //host does not have a majority, restart elections
                System.out.println("##--## The election was not successfull. #Participants: " + 
                        nrOfParticipants + " #MostVotes: " + mostVotedHost.getNrOfVotes() +" ##--##");
                Election.initElection(); //restart election                
            }
        }
    }
    @Override
    public void run() {
        startElection();        
    }

	public static void initElection() {
        String command = Commands.constructElectionMessage(Commands.startElection, Misc.processID, new Date().getTime());
		Message electionsStart = new Message(Message.MessageType.mHostCommand, command);
		Server.messageController.queueSend.push(electionsStart);
		
	}

	/**
	 * Checks if the election message belongs to the current election.
	 * 
	 * This means that the processID and time of the latest starter
	 * will be compared with the processID and time of the starter
	 * of this message.
	 * @param message
	 * @return
	 */
	public boolean isMessageForCurrentElection(Message message) {
    	String starterProcessID = Commands.getStarterProcessID(message);
    	long starterTime = Commands.getStarterTime(message);
		return starterProcessID.equals(this.starterProcessID) && this.starterElectionTime == starterTime;
	}
	
	public String getStarterProcessID() {
		return starterProcessID;
	}
	
	public void setStarterProcessID(String starterProcessID) {
		this.starterProcessID = starterProcessID;
	}
	
	public long getStarterElectionTime() {
		return starterElectionTime;
	}
	
	public void setStarterElectionTime(long starterElectionTime) {
		this.starterElectionTime = starterElectionTime;
	}

}
