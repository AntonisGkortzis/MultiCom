
package sharedresources;


/**
 * This class is used to store information for a client of the type:
 *  - unique process id,
 *  - username
 */
public class ConnectedClient {
	private String user;
    private String processID;
	
	public ConnectedClient(){}
	
	public ConnectedClient(String processID, String user){
		this.processID = processID;
		this.user = user;		
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
	
}
