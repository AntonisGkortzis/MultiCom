package sharedresources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import sharedresources.Misc.MessageType;

/**
 * This class is used to listen for messages send with Multicast to multiple hosts
 * Messages are of type:
 * 	- Master listening for request commands from clients wanting to join the network
 *  - Hosts listening for master which needs to add new clients
 *  - Hosts listening for master requesting a life sign
 *  - Master listening for hosts requesting a life sign
 *  - Hosts/Master listening for distribution of chat messages
 *  
 * @author mark
 *
 */
public class OneToManyListener implements Runnable {
	
	private MulticastSocket socket;
	private InetAddress group;
    private MessageController messageController;
    private MessageController statusUpdatesMessageController;
    private boolean isHost;

	public OneToManyListener(MessageController messageController, MessageController statusUpdatesMessageController, boolean isHost) {
	    this.messageController = messageController;
	    this.statusUpdatesMessageController = statusUpdatesMessageController;
	    this.isHost = isHost;
	    
        try {
            socket = new MulticastSocket(Config.hostMultiCastGroup);
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
    
	@Override
	public void run() {
        try {
            while(true) {
                byte[] incomingData = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try{
                	Message received = (Message)is.readObject();  
                	handleMessage(received);
                	
                } catch(ClassNotFoundException ex) {
                	ex.printStackTrace();
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        socket.close();		
	}
	
	private void handleMessage(Message receivedMessage) {
		//Do not parse your own Messages
		if(receivedMessage.getProcessID().equals(Misc.getProcessID())){
			return;
		}
//    	System.out.println("OneToManyListener received: " + receivedMessage.getText());
    	if(isHost && Config.master) {
    		if(Commands.messageIsOfCommand(receivedMessage, Commands.connectRequest)) {
//    			System.out.println("Hello " +receivedMessage.getUsername() + " with process ID " + receivedMessage.getProcessID() 
//    					+ "\nI will find a suitable host to connect to, wait a minute...");
//    			String command = Commands.constructCommand(Commands.findHost);
//    			Message message = new Message(MessageType.multipleReceivers, true, Misc.getProcessID(), "master", command);
//    			this.messageController.push(message);
    			AvailableHost suitableHost = AvailableHostsList.findSuitableHost();
    			if(suitableHost!=null) { //TODO search again if null?
//    			    System.out.println(suitableHost.toString());
    			    String command = Commands.constructCommand(Commands.hostFound, Commands.constructHostFound(suitableHost, receivedMessage.getProcessID()));
    			    Message message = new Message(MessageType.multipleReceivers, true, Misc.getProcessID(), "master", command);
    			    this.messageController.push(message);
    			    System.out.println("@OneToManyListener\n\tPushed " + message.getText());
    			}
    		}
    	}
    	if(isHost && Commands.messageIsOfCommand(receivedMessage, Commands.statusUpdate)) {
    		System.out.println("@OneToManyListener\n\tStatus update received from host " +receivedMessage.getProcessID());
    		statusUpdatesMessageController.push(receivedMessage);
    		
    	    AvailableHost availableHost = Commands.getStatus(receivedMessage);
    	    if(!AvailableHostsList.hostExists(availableHost)) {
    	        AvailableHostsList.addHost(availableHost);
    	    }
//    	    System.out.println("handle status update: ");
//    	    AvailableHostsList.printHostAddresses();
    	} else if(!isHost && Commands.messageIsOfCommand(receivedMessage, Commands.hostFound)) {
    	    System.out.println(" HOST IS FOUND " + receivedMessage.getText() + " " + Misc.getProcessID());
    	    String[] messageParts = Commands.splitMessage(receivedMessage);
    	    if(Misc.getProcessID().equals(messageParts[1])) { //This client requested a connection
    	        Config.connectToPortFromHost = Integer.parseInt(messageParts[3]);
    	        System.out.println("Connect to port: " + Config.connectToPortFromHost);
    	        Misc.unlockWaiter();
    	        
    	    }

    	}
	}
}
