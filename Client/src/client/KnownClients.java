package client;

import java.util.HashSet;
import java.util.Hashtable;

public class KnownClients {
	private Hashtable<Object, Object> clientColors;
	private HashSet<String> knownClients;
	private String colors[] = {"Black","Red","Blue","Maroon","Green","Orange","Purple",
			"Navy","Silver","Gray","Aqua","Olive","Lime","Fuchsia","Yellow"};
			
	private static int counter = 0;
	
	public KnownClients(){
		this.clientColors = new Hashtable<>();
		this.knownClients = new HashSet<>();
	}
	
	public boolean clientExists(String client){
		if(Client.getUserName().equals(client)){
			return true;
		}
		boolean flag = this.knownClients.add(client);
		
		if(flag) {
			addNewClient(client);
		}
		
		return flag; 
	}
	
	private void addNewClient(String client) {
		clientColors.put(client, new Integer(++counter));
	}

	public String getColor(String client){
		if(Client.getUserName().equals(client)) 
			return colors[0];
		else
			return colors[(int)clientColors.get(client)];
	}
	
}
