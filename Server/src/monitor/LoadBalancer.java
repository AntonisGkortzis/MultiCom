package monitor;

import multithread.sockets.Server;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Host;
import sharedresources.HostsList;
import sharedresources.Message;

/**
 * This class will make sure that every host has on average the same
 * number of clients. If a host has too much clients, the clients must
 * be connected to another host.
 * @author mark
 *
 */
public class LoadBalancer implements Runnable {

    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        boolean flag=true;
        while(flag) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Only the master can perform load balancing and not during election
            if(!Config.master && !Server.electionState.equals(Server.ElectionStates.normal)) continue;
            
            double total = 0;
            Host hostMaxNrOfClients = HostsList.getHostsList().get(0);
            Host hostMinNrOfClients = HostsList.getHostsList().get(0);
            for(Host host: HostsList.getHostsList()) {
                total+=host.getNrOfClients();
                if(host.getNrOfClients()<hostMinNrOfClients.getNrOfClients()) {
                    hostMinNrOfClients = host;             }
                if(host.getNrOfClients()>hostMaxNrOfClients.getNrOfClients()) {
                    hostMaxNrOfClients = host;
                }
            }
            //Used rounding upwards to prevent rerouting indefinitely
            double averageNrOfClients = Math.ceil(total/HostsList.size());
            
            int nrToReroute =hostMaxNrOfClients.getNrOfClients() - (int)averageNrOfClients;
            
            //Reroute nrToReroute clients from MaxHost to MinHost 
            if(total>1 && nrToReroute>0 && !hostMaxNrOfClients.getProcessID().equals(hostMinNrOfClients.getProcessID())) {
                System.out.println("Master orders to reroute " + nrToReroute +" client(s), from " +
                        hostMaxNrOfClients.getProcessID() + " to " + hostMinNrOfClients.getProcessID());
                String command = Commands.constructCommandLoadBalance(hostMaxNrOfClients, hostMinNrOfClients, nrToReroute);
                Message message = new Message(Message.MessageType.mHostCommand, command);
                Server.messageController.queueSend.push(message);
            }
            
        }

    }

}
