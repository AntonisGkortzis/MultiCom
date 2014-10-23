package sharedresources;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to maintain a list of connected clients to a host. 
 * The purpose of this class is to know to which clients acknowledgments have to be send
 */
public class ConnectedClientsList {
	private static List<ConnectedClient> clients = new ArrayList<>();
	
	public ConnectedClientsList(){}
	
	public static void addClient(ConnectedClient client){
		clients.add(client);
	}
	
	public static boolean clientExists(String processID){
		for(ConnectedClient client : clients){
			if(client.getProcessID().equals(processID)) {
				return true;
			}
		}
		return false;
	}
	
	public static int size() {
	    return clients.size();
	}

}
