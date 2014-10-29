package sharedresources;

import java.util.ArrayList;
import java.util.Iterator;


public class ForwardMessage {

    private Message message;
    private long id;
    private ArrayList<ClientAmountSendPair> clients;
    
    public ForwardMessage(Message message, long messageId) {
        this.message = message;
        this.id = messageId;
        clients = new ArrayList<ClientAmountSendPair>();
        for(ConnectedClient client: ConnectedClientsList.clients) {
            this.addClient(client);
        }
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
    
    /**
     * Increases the number of times a message is send for the client with a specific processID.
     * If the number of times send exceeds 2 then the client is removed from the list.
     * @param processID
     */
//    public void incAmountSend(String processID) {
//        Iterator<ClientAmountSendPair> iterator = clients.iterator();
//        ClientAmountSendPair pair = null;
//        while(iterator.hasNext()) {
//            pair = iterator.next();
//            if(pair.isClient(processID)) {
//                System.out.println("@@ IncAmountSend nrOfTimes: " + pair.getNrOfRetries());
//                if(pair.getNrOfRetries()>2) {
//                    iterator.remove();
//                } else {
//                    pair.incNrOfRetries();
//                }
//                break;
//            }
//        }
//    }
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

}
