package sharedresources;

import java.util.Date;

/**
 * Information about host
 * @author mark
 *
 */
public class Host {
    private String address;
    private int port;
    private String processID;
    private boolean isMaster;
    private int nrOfClients;
    private Date lastUpdate;
	private int votes;
    
    public Host(){}
    
    public Host(int nrOfClients, String address, int port, boolean isMaster){
        this.setNrOfClients(nrOfClients);
        this.setAddress(address);
        this.setPort(port);
        this.setMaster(isMaster);
        this.setProcessID(Misc.processID);
        this.votes=0;
    }
    
    public Host(int nrOfClients, String address, int port, boolean isMaster, String processID){
    	this(nrOfClients, address, port, isMaster);
    	this.processID = processID;
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }
    
    public void setNrOfVotes(int vote){
    	this.votes = vote;
    }

    public int getNrOfClients() {
        return nrOfClients;
    }

    public void setNrOfClients(int nrOfClients) {
        this.nrOfClients = nrOfClients;
    }
    
    @Override
    public String toString() {
        return "-- HostINFO: " + this.nrOfClients  + " " + this.address + " " + this.port + " " + this.isMaster + " " + this.processID;
    }

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getNrOfVotes() {
		return this.votes;
	}


        
}
