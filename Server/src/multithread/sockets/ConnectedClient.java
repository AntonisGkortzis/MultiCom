
package multithread.sockets;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;


/**
 * This class is used to store information for a client of the type:
 *  - socket,
 *  - ip address,
 *  - unique process id,
 *  - username
 */
public class ConnectedClient {
	private Socket socket;
	private String address;
	private String user;
    private String processID;
	
	public ConnectedClient(){}
	
	public ConnectedClient(Socket socket, String address, String user){
		this.socket = socket;
		this.address = address;
		this.user = user;		
	}
	
	public void updateClient(String message){
		try {
			PrintStream out = new PrintStream(socket.getOutputStream());
			out.println("Server: I got:" + message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setSocket(Socket socket){
		this.socket = socket;
	}
	
	public void setAddress(String address){
		this.address = address;
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
	
	public String getAddress(){
		return this.address;
	}
	
	public String getProcessID() {
	    return this.processID;
	}
	
}
