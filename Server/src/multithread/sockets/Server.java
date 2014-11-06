package multithread.sockets;
import monitor.ClientMonitor;
import monitor.HoldbackQueueMonitorFromClient;
import monitor.HoldbackQueueMonitorFromHost;
import monitor.LoadBalancer;
import monitor.ReceivedAcknowledgmentsByHostFromClientsMonitor;
import monitor.ReceivedAcknowledgmentsByHostFromMHostsMonitor;
import monitor.StatusMonitor;
import sender.HostToClientAckSender;
import sender.SendStatusUpdate;
import sharedresources.Commands;
import sharedresources.Message;
import sharedresources.MessageController;
import sharedresources.Misc;
import sharedresources.OneToManyListener;

/**
 * This class is used to start a server and initiates all the connections.
 * The types of connections created are:
 *  - Host to client communication
 *  - Host to multiple clients communication
 *  - Host to multiple hosts communication
 *
 */
public class Server {

    public static int port; //port for one to one connections with clients
    public static String address;
    
    public static MessageController messageController = new MessageController();
   
    public static enum ElectionStates {
    	normal, // when no election is taking place
    	voting, //upon receiving the "startElection" command
    	voted, // Once a vote is made
    }
    
    public static ElectionStates electionState = ElectionStates.normal;
    
    // Listen for incoming connections and handle them
    public static void main(String[] args) {
        
        ClientMonitor clientMonitor = new ClientMonitor();
        clientMonitor.start();
        
        HoldbackQueueMonitorFromClient holdbackQueueMonitorFromClient = new HoldbackQueueMonitorFromClient();
        holdbackQueueMonitorFromClient.start();
        
        HoldbackQueueMonitorFromHost holdbackQueueMonitorFromHost = new HoldbackQueueMonitorFromHost();
        holdbackQueueMonitorFromHost.start();
        
        //One to one communication between host and client
        HostToClient hostToClient = new HostToClient();
        hostToClient.start();       
        
        System.out.println("############# Host Up & Running #############");
        System.out.println("##### Process ID: " + Misc.processID );
		System.out.println("##### Local Multicast port: " + Server.port);
		System.out.println("#############################################");
		System.out.println("\n");
        
        ReceivedAcknowledgmentsByHostFromClientsMonitor receivedAcknowledgmentsByHostFromClientsMonitor = new ReceivedAcknowledgmentsByHostFromClientsMonitor();
        receivedAcknowledgmentsByHostFromClientsMonitor.start();
        
        ReceivedAcknowledgmentsByHostFromMHostsMonitor receivedAcknowledgmentsByHostFromHostsMonitor = new ReceivedAcknowledgmentsByHostFromMHostsMonitor();
        receivedAcknowledgmentsByHostFromHostsMonitor.start();
        
        HostToClientAckSender hostToClientAckSender = new HostToClientAckSender();
        hostToClientAckSender.start();
        
        //One to many communication between host and its clients
        HostToMClient hostToMClient = new HostToMClient();
        hostToMClient.start();

        //Sending with global multicast
        HostToMHost hostToMHost = new HostToMHost();
        hostToMHost.start();
        
        SendStatusUpdate sendPing = new SendStatusUpdate();
        sendPing.start();
        
        //Listen to global multicast
        OneToManyListener oneToManyListener = new OneToManyListener(messageController, true);
        oneToManyListener.start();
        
        //Keep track of the status of the hosts
        StatusMonitor statusMonitor = new StatusMonitor();
        statusMonitor.start();
        
        //Requesting Status updates from already existing host in order to build the HostsList
        Message statusRequest = new Message(Message.MessageType.mHostCommand, Commands.requestStatusUpdate);
        messageController.queueSend.push(statusRequest);
        
        
        // For starting Elections on host's start up
//        Election.initElection();
        
        LoadBalancer loadBalancer = new LoadBalancer();
        loadBalancer.start();
    }
   
}