package sharedresources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ForwardMessage {

    private Message message;
    private long id;
    private ArrayList<ClientAmountSendPair> clients;
    private ArrayList<HostAmountSendPair> hosts;
    private boolean hostIsReceiver;
    
    public ForwardMessage(Message message, long messageId, boolean hostIsReceiver) {
        this.message = message;
        this.id = messageId;
        clients = new ArrayList<ClientAmountSendPair>();
        hosts = new ArrayList<HostAmountSendPair>();
        this.hostIsReceiver = hostIsReceiver;
        if(hostIsReceiver){
        	System.out.println(" -- Creating Forward message for hosts -- ");
        	for(Host host: HostsList.getHostsList()) {
	            this.addHost(host);
	        }
        } else {
        	System.out.println(" -- Creating Forward message for clients -- ");
        	for(ConnectedClient client: ConnectedClientsList.clients) {
	            this.addClient(client);
	        }
        }
    }
    
    private void addHost(Host host) {
    	HostAmountSendPair pair = new HostAmountSendPair(host);
        this.hosts.add(pair);
		
	}

	public void addClient(ConnectedClient client) {
        ClientAmountSendPair pair = new ClientAmountSendPair(client);
        this.clients.add(pair);
    }
    
    /**
     * A client should be removed once an ack is received belonging to this message
     * @param processID
     */
    public boolean removeClient(String processID) {
        Iterator<ClientAmountSendPair> iterator = clients.iterator();
        ClientAmountSendPair pair = null;
        while(iterator.hasNext()) {
            pair = iterator.next();
            if(pair.isClient(processID)) {
                iterator.remove();
                return clients.size() == 0;
            }
        }
        return false;
    }
    
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<ClientAmountSendPair> getClients() {
        return clients;
    }

    public void setClients(ArrayList<ClientAmountSendPair> clients) {
        this.clients = clients;
    }

	public ArrayList<HostAmountSendPair> getHosts() {
		return hosts;
	}

	public boolean removeHost(String processID) {
		Iterator<HostAmountSendPair> iterator = hosts.iterator();
        HostAmountSendPair pair = null;
        while(iterator.hasNext()) {
            pair = iterator.next();
            if(pair.isHost(processID)) {
                iterator.remove();
                return hosts.size() == 0;
            }
        }
        return false;
	}

}
