package sharedresources;

public class HostAmountSendPair {
	private Host host;
    private int nrOfRetries;
    
    public HostAmountSendPair(Host host) {
        this.setHost(host);
        this.nrOfRetries = 0;
    }

    public boolean isHost(String processId) {
        return host.getProcessID().equals(processId);
    }
    
    public int getNrOfRetries() {
        return nrOfRetries;
    }

    public void incNrOfRetries() {
        this.nrOfRetries++;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
