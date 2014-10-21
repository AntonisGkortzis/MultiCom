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
    private boolean isHost;

	public OneToManyListener(MessageController messageController, boolean isHost) {
	    this.messageController = messageController;
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
   
    
    	if(receivedMessage.getMessageType().equals(MessageType.mHostCommand)) {
    	    messageController.queueMHostsCommand.push(receivedMessage);
    	} else if(receivedMessage.getMessageType().equals(MessageType.mHostChat))  {
    	    messageController.queueMHostsChat.push(receivedMessage);
    	} else if(receivedMessage.getMessageType().equals(MessageType.mHostVote)) {
    	    messageController.queueMHostsVote.push(receivedMessage);
    	} else if(receivedMessage.getMessageType().equals(MessageType.mClientCommand)) {
    	    System.out.println("One to many: mClientCommand");
    	    messageController.queueMClientCommand.push(receivedMessage);
    	} 
	}
}
