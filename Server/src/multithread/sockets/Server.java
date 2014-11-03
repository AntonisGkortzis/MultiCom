package multithread.sockets;
import monitor.ClientMonitor;
import monitor.HoldbackQueueMonitorFromClient;
import monitor.HoldbackQueueMonitorFromHost;
import monitor.LoadBalancer;
import monitor.ReceivedAcknowledgmentsByHostFromClientsMonitor;
import monitor.ReceivedAcknowledgmentsByHostFromMHostsMonitor;
import monitor.StatusMonitor;
import sender.HostHeartbeatToMClient;
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
    public static int mPort; // port for multicast message to clients
    public static String address;
    
    public static MessageController messageController = new MessageController();
   
    public static enum ElectionStates {
    	normal, // when no election is taking place
    	voting, //upon receiving the "startElection" command
    	voted, // Once a vote is made
    	finished // The election is finished and a new leader is born
    }
    
    public static ElectionStates electionState = ElectionStates.normal;
    
    // Listen for incoming connections and handle them
    public static void main(String[] args) {
        System.out.println("Server Running with processID " + Misc.processID + " and port " + port + " ...");
        
        ClientMonitor clientMonitor = new ClientMonitor();
        clientMonitor.start();
        
        HoldbackQueueMonitorFromClient holdbackQueueMonitorFromClient = new HoldbackQueueMonitorFromClient();
        holdbackQueueMonitorFromClient.start();
        
        HoldbackQueueMonitorFromHost holdbackQueueMonitorFromHost = new HoldbackQueueMonitorFromHost();
        holdbackQueueMonitorFromHost.start();
        
        //One to one communication between host and client
        HostToClient hostToClient = new HostToClient();
        hostToClient.start();       
        
//        OneToOneListener oneToOneListener = new OneToOneListener(hostToClient.getSocket(), Server.messageController);
//        oneToOneListener.start();
        
        ReceivedAcknowledgmentsByHostFromClientsMonitor receivedAcknowledgmentsByHostFromClientsMonitor = new ReceivedAcknowledgmentsByHostFromClientsMonitor();
        receivedAcknowledgmentsByHostFromClientsMonitor.start();
        
        //TODO uncomment this to test the 3rd lvl of reliability.. Not yet working
        ReceivedAcknowledgmentsByHostFromMHostsMonitor receivedAcknowledgmentsByHostFromHostsMonitor = new ReceivedAcknowledgmentsByHostFromMHostsMonitor();
        receivedAcknowledgmentsByHostFromHostsMonitor.start();
        
        HostToClientAckSender hostToClientAckSender = new HostToClientAckSender();
        hostToClientAckSender.start();
        
        //One to many communication between host and its clients
        HostToMClient hostToMClient = new HostToMClient();
        hostToMClient.start();

        //Send heartbeats to clients of local multicast //TODO remove
//        HostHeartbeatToMClient hostHeartbeatToMClient = new HostHeartbeatToMClient();
//        hostHeartbeatToMClient.start();
        
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
        
        //A waiting time (Thread.sleep(2000)) exists at the beginning of the elections.
        
        // For starting Elections on host's start up
//        Election.initElection();
        
        LoadBalancer loadBalancer = new LoadBalancer();
        loadBalancer.start();
        
    }
   
}