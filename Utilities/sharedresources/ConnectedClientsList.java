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
	
//	@Deprecated
//	public boolean clientExists(ConnectedClient client){
//		for(ConnectedClient connectedclient : clients){
//			if(client.getAddress().equals(connectedclient.getAddress())
//					&& client.getUser().equals(connectedclient.getUser())){
//				return true;
//			}
//		}
//		return false;
//	}
	
	public static boolean clientExists(String processID){
		for(ConnectedClient client : clients){
			if(client.getProcessID().equals(processID)) {
				return true;
			}
		}
		return false;
	}
	
//	public void printClientAddresses(){
//		System.out.println("-- Connected Clients --");
//		for(ConnectedClient client : clients){
//			System.out.println("\t" + client.getAddress() + " : " + client.getUser());
//		}
//	}
	
//	public void updateClients(String message){
//		for(ConnectedClient client : clients){
//			client.updateClient(message);
//		}
//	}
	
	public static int size() {
	    return clients.size();
	}

}
