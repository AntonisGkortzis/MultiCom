package sharedresources;


public class ClientAmountSendPair {
    private ConnectedClient client;
    private int nrOfRetries;
    
    public ClientAmountSendPair(ConnectedClient client) {
        this.setClient(client);
        this.nrOfRetries = 0;
    }

    public boolean isClient(String processId) {
        return client.getProcessID().equals(processId);
    }
    
    public int getNrOfRetries() {
        return nrOfRetries;
    }

    public void incNrOfRetries() {
        this.nrOfRetries++;
    }

    public ConnectedClient getClient() {
        return client;
    }

    public void setClient(ConnectedClient client) {
        this.client = client;
    }
    
    
}
