package multithread.sockets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import sharedresources.AvailableHost;
import sharedresources.AvailableHostsList;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;
import sharedresources.MessageController;
import sharedresources.Misc;
import sharedresources.Misc.MessageType;

/**
 * This class is used for communication between a host and multiple hosts.
 *  - Normal hosts need to be able to send a broadcast message to detect if the master is still alive.
 *  - The master needs to send a broadcast message to all the hosts to see if one host died.
 *  - The master needs to send a broadcast message to find a suitable host (including itself) for a client
 *  - Hosts need to send chat messages to other hosts
 */
public class HostToMHost implements Runnable{

    private DatagramSocket socket;

	public HostToMHost() {	    
        try {
            socket = new DatagramSocket(0);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SendStatusUpdate sendPing = new SendStatusUpdate();
        sendPing.start();
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() { //TODO MAke sure that this is only sending chat messages (distributing)
        boolean flag=true;
        while (flag) {
  
            Message message = Server.messageController.queueMHostsChat.pop();

            if(message != null){
            	flag=sendMessage(message);
            }
            
            message = Server.messageController.queueMClientCommand.pop();

            if(message != null){
                flag=sendMessage(message);
            }
            
            message = Server.messageController.queueMHostsCommand.pop();
            if(message != null) {
                if(Config.master) {
                	if(Commands.messageIsOfCommand(message, Commands.connectRequest)) {
                        AvailableHost suitableHost = AvailableHostsList.findSuitableHost();
                        if(suitableHost!=null) { //TODO search again if null?
                            String command = Commands.constructCommand(Commands.hostFound, Commands.constructHostFound(suitableHost, message.getProcessID()));
                            Message newMessage = new Message(MessageType.mClientCommand, true, Misc.getProcessID(), "master", command);
                            Server.messageController.queueMClientCommand.push(newMessage);
                            System.out.println("@HostToMHost\n\tPushed " + newMessage.getText());
                        }
                    }
                }
                
                if(Commands.messageIsOfCommand(message, Commands.statusUpdate)) { //Receive status updates
                    System.out.println("@HostToMHost\n\tStatus update is going to be sent from host " + message.getProcessID());
    //                messageController.queueMHostsCommand.push(message);
                    //TODO add respond on request for status update
                    AvailableHost availableHost = Commands.getStatus(message);
                    if(!AvailableHostsList.hostExists(availableHost)) {
                        AvailableHostsList.addHost(availableHost);
                    }
        //          System.out.println("handle status update: ");
        //          AvailableHostsList.printHostAddresses();
        
                } else if(Commands.messageIsOfCommand(message, Commands.startElection)) {
                    Election election = new Election(this);
                }
            }
            
            
            
            try {
                Thread.sleep(500); //TODO put delay in config. Must be faster than push from ping for now
            } 
            catch (InterruptedException e) { 
                e.printStackTrace();
                flag=false;
            }
        }
   
        
        socket.close();    
        
    }
        
    public boolean sendMessage(Message message) {
        InetAddress group;
        try {
            group = InetAddress.getByName(Config.multiCastAddress);
            System.out.println("@HostToMultipleHosts\n\tSending message: " + message.getText());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(message);
            byte[] data = outputStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, group, Config.hostMultiCastGroup);
            socket.send(packet);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
