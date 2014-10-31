package sharedresources;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ForwardMessage {

    private Message message;
    private long id;
    private BlockingQueue<ClientAmountSendPair> clients;
    private BlockingQueue<HostAmountSendPair> hosts;
    
    public ForwardMessage(Message message, long messageId, boolean hostIsReceiver) {
        this.message = message;
        this.id = messageId;
        clients = new LinkedBlockingQueue<ClientAmountSendPair>();
        hosts = new LinkedBlockingQueue<HostAmountSendPair>();
        
        if(!hostIsReceiver) {
            System.out.println(" -- Creating Forward message for clients -- ");
        	for(ConnectedClient client: ConnectedClientsList.clients) {
                this.addClient(client);
            }
        } else {
            System.out.println(" -- Creating Forward message for hosts -- ");
            for(Host host: HostsList.getHostsList()) {
                if(Misc.processID.equals(host.getProcessID())) continue;
                this.addHost(host);
            }
        }
    }

	public void addClient(ConnectedClient client) {
        ClientAmountSendPair pair = new ClientAmountSendPair(client);
        this.clients.add(pair);
    }
	
    private void addHost(Host host) {
        HostAmountSendPair pair = new HostAmountSendPair(host);
        this.hosts.add(pair);
    }
    
    public BlockingQueue<ClientAmountSendPair> getClients() {
        return clients;
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

    public BlockingQueue<HostAmountSendPair> getHosts() {
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

}
