package client;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

public class KnownClients {
	private Hashtable<Object, Object> clientColors;
	private HashSet<String> knownClients;
	private String colors[] = {"Black","Red","Orange","Yellow","Purple",
			"Blue","Maroon","Navy","Silver","Green",
			"Gray","Aqua","Olive","Lime","Fuchsia"};
	private static int counter = 0;
	
	public KnownClients(){
		this.clientColors = new Hashtable<>();
		this.knownClients = new HashSet<>();
	}
	
	public boolean clientExists(String client){
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
		return colors[(int)clientColors.get(client)];
	}
	
}
