package client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;
import sharedresources.Misc;
import sharedresources.Message.MessageType;
import utils.CRC32Calculator;

/**
 * This class is used for listening to messages send by MultiCast from a host.
 * Information received is from the type:
 *  - host information for creating the initial connection (messages not concerning the current client will be ignored)
 *  - Chat messages
 *
 */
public class MClientListener implements Runnable {
    
    private InetAddress group;
    private MulticastSocket socket;
    private Client client;
    private boolean flag;
    
    public MClientListener(Client client) {
        this.client = client;
        try {
            socket = new MulticastSocket(Config.connectToPortFromHost );
            group = InetAddress.getByName(Config.multiCastAddress);
            socket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    public void stop() {
        this.flag=false;
    }
    @Override
    public void run() {
        this.flag=true;
        try {
            while(flag) {
                byte[] incomingData = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try{
                	Message message = (Message)is.readObject();  
                	
                	if(Commands.messageIsOfCommand(message, Commands.connectToNewHost)) {
                	    String[] messageParts = Commands.splitMessage(message);
                	    String processId = messageParts[1];
                	    if(processId.equals(Misc.processID)) {
                	    	int port = Integer.parseInt(messageParts[3]);
                	        System.out.println("@@-- I will reconnect to new Host " + message.getProcessID() + "/" + port + " --@@");
                	        String address = messageParts[2];
                	        this.connectToDifferentHost(address, port);
                	        System.out.println("##-- Now connected to Host " + message.getProcessID() + "/" + port + " --##");
                	    }
                	// Update the time of when the host was last seen
                	} else if (Commands.messageIsOfCommand(message, Commands.hostHeartbeat)) {
                	    client.lastHostUpdate = new Date().getTime();
                	} else {
                    	if(Commands.messageIsOfCommand(message, Commands.targetedResentMessage)) {
                    		if(!Commands.getStarterProcessID(message).equals(Misc.processID)){
                    			continue;
                    		} else {
                    			message.setText(Commands.getTextParseTargetedMessageText(message));
                    		}
                    	}
                    	System.out.println("##-- Messenger received: '" + message.getText() + "' with id: " + message.getId() + " --##");
                    	
                    	if(message.getChecksum() == CRC32Calculator.getChecksum(message.getText())){
	                    	//create the acknowledgement and store it in the queueAcknowledgments
	                        String command = Commands.constructCommand(Commands.acknowledgement, Long.toString(message.getId()));
	                        Message ack = new Message(Message.MessageType.acknowledgement, command);
	                        client.messageController.queueAcknowledgements.push(ack); //Comment this if you want to test the host retries
	
	                        //Store in holdback queue
	                        message.setTimeReceived(new Date().getTime());
	                        this.client.holdbackQueue.put(message);
                    	} else {
                    		System.out.println("##-- Message is corrupted - CRC32 verification failed --##");
                    	}
                	}
                	
                } catch(ClassNotFoundException ex) {
                	ex.printStackTrace();
                }
                Thread.sleep(250);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }

    private void connectToDifferentHost(String address, int port) {
        //First handle every message in the holdbackQueue
        client.unloadHoldbackQueue();
        
        //Then send a shutdown message to let current host know you are disconnecting
        String command = Commands.constructCommand(Commands.clientShutdown);
        Message shutdownMsg = new Message(MessageType.clientCommand, command);
        client.clientToHost.sendMessage(shutdownMsg);
        //Okay connection closed. To the new connection!
        Config.connectToPortFromHost = port;
        client.startConnection();
    }
}
