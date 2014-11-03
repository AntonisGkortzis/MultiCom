package client;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

public class KnownClients {
	private Hashtable clientColors;
	private HashSet<String> knownClients;
	
	public KnownClients(){
		this.clientColors = new Hashtable<>();
		this.knownClients = new HashSet<>();
		
		System.out.println("==== intitializng knownClinetsList ====");
		
	}
	
	public boolean clientExists(String client){
		boolean flag = this.knownClients.add(client);
		
		if(flag)
			addNewClient(client);
		
		return flag; 
	}
	
	private void addNewClient(String client) {
		// TODO Auto-generated method stub
		System.out.println("==== Adding new client : "+client+"=====");
		
	}

	public int getColor(String client){
		//return the integer for the given client
		return (int)clientColors.get(client);
	}
	
}
