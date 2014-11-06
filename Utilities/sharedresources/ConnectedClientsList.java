package sharedresources;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is used to maintain a list of connected clients to a host. 
 * The purpose of this class is to know to which clients acknowledgments have to be send
 */
public class ConnectedClientsList {
	public static BlockingQueue<ConnectedClient> clients = new LinkedBlockingQueue<>();
	
	public ConnectedClientsList(){}
	
	public static void addClient(ConnectedClient client){
		clients.add(client);
	}
	
	/**
	 * Returns true if a client with processID exists
	 * @param processID
	 * @return
	 */
	public static boolean clientExists(String processID){
		for(ConnectedClient client : clients){
			if(client.getProcessID().equals(processID)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Updates the Date of last seen of a client with processID.
	 * May need to be extended in the future
	 * @param processID
	 */
	public static void updateClient(String processID) {
	    for(ConnectedClient client: clients) {
	        if(client.getProcessID().equals(processID)); {
	            client.setLastUpdate(new Date());
	        }
	    }
	}

	/**
	 * Removes client with processID
	 * @param processID
	 */
    public static void removeClient(String processID) {
        Iterator<ConnectedClient> iterator = clients.iterator();
        while(iterator.hasNext()) {
            ConnectedClient client = iterator.next();
            if(client.getProcessID().equals(processID)) {
                iterator.remove();
            }
        }
    }

    public static void addHoldBackMessage(Message message) {
        for(ConnectedClient client: clients) {
            if(client.getProcessID().equals(message.getProcessID())) {
                try {
                    client.holdbackQueue.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static int size() {
        return clients.size();
    }
}
