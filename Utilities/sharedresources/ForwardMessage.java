package sharedresources;

import java.util.ArrayList;
import java.util.Iterator;


public class ForwardMessage {

    public Message message;
    public long messageId;
    public ArrayList<ClientAmountSendPair> clients;
    
    public ForwardMessage(Message message, long messageId) {
        this.message = message;
        this.messageId = messageId;
        clients = new ArrayList<ClientAmountSendPair>();
    }
    
    public void addClient(ConnectedClient client) {
        ClientAmountSendPair pair = new ClientAmountSendPair(client);
        this.clients.add(pair);
    }
    
    /**
     * A client should be removed once an ack is received belonging to this message
     * @param processID
     */
    public void removeClient(String processID) {
        Iterator<ClientAmountSendPair> iterator = clients.iterator();
        ClientAmountSendPair pair = null;
        while(iterator.hasNext()) {
            pair = iterator.next();
            if(pair.isClient(processID)) {
                iterator.remove();
                break;
            }
        }
    }
    
    /**
     * Increases the number of times a message is send for the client with a specific processID.
     * If the number of times send exceeds 2 then the client is removed from the list.
     * @param processID
     */
    public void incAmountSend(String processID) {
        Iterator<ClientAmountSendPair> iterator = clients.iterator();
        ClientAmountSendPair pair = null;
        while(iterator.hasNext()) {
            pair = iterator.next();
            if(pair.isClient(processID)) {
                if(pair.getNrOfRetries()>2) {
                    iterator.remove();
                } else {
                    pair.incNrOfRetries();
                }
                break;
            }
        }
    }
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public ArrayList<ClientAmountSendPair> getClients() {
        return clients;
    }

    public void setClients(ArrayList<ClientAmountSendPair> clients) {
        this.clients = clients;
    }

}
