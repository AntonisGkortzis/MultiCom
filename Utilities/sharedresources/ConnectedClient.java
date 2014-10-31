
package sharedresources;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * This class is used to store information for a client of the type:
 *  - unique process id,
 *  - username
 */
public class ConnectedClient {
	private String user;
    private String processID;
    private Date lastUpdate; //TODO explain in report
	public BlockingQueue<Message> holdbackQueue; //TODO explain in report
	
	public ConnectedClient(){}
	
	public ConnectedClient(String processID, String user){
		this.processID = processID;
		this.user = user;		
		this.setLastUpdate(new Date());
		this.holdbackQueue = new PriorityBlockingQueue<Message>();
	}
	
	public void setUser(String user){
		this.user = user;
	}

	public void setProcessID(String processID) {
        this.processID = processID;
    }

    public String getUser(){
		return this.user;
	}
	
	public String getProcessID() {
	    return this.processID;
	}

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    	
}
