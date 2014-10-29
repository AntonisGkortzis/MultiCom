
package sharedresources;

import java.net.Socket;


/**
 * This class is used to store information for a client of the type:
 *  - unique process id,
 *  - username
 */
public class ConnectedClient {
	private String user;
    private String processID;
    private Socket socket;
	
	public ConnectedClient(){}
	
	public ConnectedClient(String processID, String user, Socket socket){
		this.processID = processID;
		this.user = user;		
		this.socket = socket;
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

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
}
