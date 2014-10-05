package sharedresources;

import java.util.ArrayList;
import java.util.List;

public  class AvailableHostsList {
    private static List<AvailableHost> hosts = new ArrayList<>();
    
    public AvailableHostsList(){}
    
    /**
     * Add a new host to the list
     * @param host
     */
    public static void addHost(AvailableHost host){
        hosts.add(host);
    }
    
    /**
     * Check if the host is known
     * @param host
     * @return
     */
    public static boolean hostExists(AvailableHost host){
        for(AvailableHost availableHost : hosts){
            if(host.getProcessID().equals(availableHost.getProcessID())){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Update the information of a host
     * Only changes number of clients and isMaster
     * @param host
     */
    public static void updateHost(AvailableHost host) {
        for(AvailableHost availableHost: hosts) {
            if(availableHost.getProcessID().equals(host.getProcessID())) {
                availableHost.setNrOfClients(host.getNrOfClients());
                availableHost.setMaster(host.isMaster());
            }
        }
    }
    public static void printHostAddresses(){
        System.out.println("-- Available Hosts --");
        for(AvailableHost host : hosts){
            System.out.println("\t" + host.getAddress() + " : " + host.getPort() + " clients: " + host.getNrOfClients());
        }
    }
    
    /**
     * Find the most suitable host.
     * This host has the least number of clients
     * @return
     */
    public static AvailableHost findSuitableHost() {
        int minimumClients = Integer.MAX_VALUE;
        AvailableHost suitableHost = null;
        for(AvailableHost host : hosts){
            if(host.getNrOfClients()<minimumClients) {
                suitableHost = host;
            }
        }
        
        return suitableHost;
    }
    
    public static int size() {
        return hosts.size();
    }
}
