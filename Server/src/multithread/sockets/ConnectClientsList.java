package multithread.sockets;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to maintain a list of connected clients to a host. 
 * The purpose of this class is to know to which clients acknowledgments have to be send
 */
public class ConnectClientsList {
	private List<ConnectedClient> clients = new ArrayList<>();
	
	public ConnectClientsList(){}
	
	public void addClient(ConnectedClient client){
		this.clients.add(client);
	}
	
	public boolean clientExists(ConnectedClient client){
		for(ConnectedClient connectedclient : clients){
			if(client.getAddress().equals(connectedclient.getAddress())
					&& client.getUser().equals(connectedclient.getUser())){
				return true;
			}
		}
		return false;
	}
	
	public void printClientAddresses(){
		System.out.println("-- Connected Clients --");
		for(ConnectedClient client : clients){
			System.out.println("\t" + client.getAddress() + " : " + client.getUser());
		}
	}
	
	public void updateClients(String message){
		for(ConnectedClient client : clients){
			client.updateClient(message);
		}
	}
	
	public int size() {
	    return this.clients.size();
	}

}
