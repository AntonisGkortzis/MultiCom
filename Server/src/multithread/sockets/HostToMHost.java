package multithread.sockets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;

import sender.SendStatusUpdate;
import sharedresources.ConnectedClient;
import sharedresources.ConnectedClientsList;
import sharedresources.Host;
import sharedresources.HostsList;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;
import sharedresources.Misc;

/**
 * This class is used for communication between a host and multiple hosts.
 *  - Normal hosts need to be able to send a broadcast message to detect if the master is still alive.
 *  - The master needs to send a broadcast message (Global Multicast) to all the hosts to see if one host died.
 *  - The master needs to send a broadcast message (Global Multicast) to find a suitable host (including itself) for a client
 *  - Hosts need to send chat messages to other hosts (Global Multicast)
 */
public class HostToMHost implements Runnable{

    private static DatagramSocket socket;
    private Election election;

	public HostToMHost() {	    
        try {
            socket = new DatagramSocket(0);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() { 
        boolean flag=true;
        while (flag) {
        	
            //Messages that are only to be sent
        	Message message = Server.messageController.queueSend.pop();
            if(message != null ){
            	flag = sendMessage(message);
            }	
        	
            //Messages that contains commands that should be parsed and executed
            message = Server.messageController.queueMHostsCommand.pop();
            if(message != null) {
            	if(Commands.messageIsOfCommand(message, Commands.connectRequest)) {
            		if(Config.master) {
                        Host suitableHost = HostsList.findSuitableHost();
                        String command = Commands.constructHostFound(suitableHost, message.getProcessID());
                        Message newMessage = new Message(Message.MessageType.mClientCommand, command);
                        newMessage.setClientAsReceiver(true);//In order not to be stored by other hosts
                        sendMessage(newMessage);
                        System.out.println("@@-- I, the Master, am directing a new Messenger "+ message.getProcessID() +" to Host: " 
                        + suitableHost.getProcessID() + "/" +suitableHost.getPort() + "--@@");
            		}
            	//If you parse a received message that requests a status update then send a status update
                } else if(Commands.messageIsOfCommand(message, Commands.requestStatusUpdate)) { 
                	//Create and Send a StatusUpdate message
                	Message statusMessage = SendStatusUpdate.createStatusMessage();
                	sendMessage(statusMessage);
                //Participate in the elections if you parse a received message about elections
                } else if(Commands.messageIsOfCommand(message, Commands.startElection)) {
                	// In case that the receiver is already in an elections then reset the elections and re-initialize them 
                	if(!Server.electionState.equals(Server.ElectionStates.normal) 
                			&& election != null){ 
                			election.stop();
                			Server.electionState = Server.ElectionStates.normal;
                	}
                	election = new Election(Server.messageController, Commands.getStarterProcessID(message), Commands.getStarterTime(message));
                	election.start();
                } else if(Commands.messageIsOfCommand(message, Commands.IAmTheMaster)) {
                	if(this.election.isMessageForCurrentElection(message)) {
	                	//Elections STEP 5a
	                	HostsList.setMasterAndResetVotes(message.getProcessID());
	                	//Elections STEP 5b
	                	Server.electionState = Server.ElectionStates.normal;
	                	System.out.println("##-- Elections are finished. Am I the Master? " + (Config.master ? "Yes":"No") + " --##");
	                	System.out.println("##################################\n");
                	}
                } else if(Commands.messageIsOfCommand(message, Commands.loadBalance)){
                	String[] messageParts = Commands.splitMessage(message);
                	String fromHostPid = messageParts[1];
                	if(fromHostPid.equals(Misc.processID)) {
                	    String toHostPid = messageParts[2];
                	    int nrOfClients = Integer.parseInt(messageParts[3]);
                	    Host toHost = HostsList.getHost(toHostPid);
                	    System.out.println("\n############# Load Balancing #############");
                	    System.out.println("##-- I received a load balance order. Re-route " + nrOfClients + " Messenger(s) to " + toHostPid + "/" + toHost.getPort() + " --##");
                	    if(toHost!=null) {
                	        Iterator<ConnectedClient> iterator = ConnectedClientsList.clients.iterator();
                	        
                	        int i = 0;
                	        while(iterator.hasNext()) {
                	            if(i>=nrOfClients) break;
                	            ConnectedClient client = iterator.next();
                	            i++;
                	            String command = Commands.constructConnectToNewHost(toHost, client.getProcessID());
                                message = new Message(Message.MessageType.clientCommand,command);
                                Server.messageController.queueHostChat.push(message);
                                System.out.println("@@-- I am ordering Messenger "+ client.getProcessID() + " to move to Host " + toHostPid + "/" + toHost.getPort() + " --@@");
                	        }
                	    }
                	    System.out.println("##########################################\n");
                	}
                }
            }
            
            //Messages that contains status messages received from hosts
            //Status updates stored in the queue (because we store them manually) should not be parsed
            message = Server.messageController.queueMHostsStatus.pop();
            if(message != null && !message.getProcessID().equals(Misc.processID)){
                Host host = Commands.getStatus(message);
                if(!HostsList.hostExists(host)) {
                	HostsList.addHost(host);
                } else {
                	HostsList.updateHost(host);
                }

            }
            
            //Messages that contains vote messages received from hosts
            message = Server.messageController.queueMHostsVote.pop();
            if(message != null 
            		&& (Server.electionState.equals(Server.ElectionStates.voting) 
            				|| Server.electionState.equals(Server.ElectionStates.voted))){

            	//Only update if this message was part of the last election
            	if(this.election.isMessageForCurrentElection(message)) {
            		HostsList.updateHostVote(Commands.getVote(message));
            	}
            }	

            
            
            try {
                Thread.sleep(150);
            } 
            catch (InterruptedException e) { 
                e.printStackTrace();
                flag=false;
            }
        }
        
        socket.close();    
        
    }
        
	public static boolean sendMessage(Message message) { //
        InetAddress group;
        try {
            group = InetAddress.getByName(Config.multiCastAddress);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(message);
            byte[] data = outputStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, group, Config.hostMultiCastGroup);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
