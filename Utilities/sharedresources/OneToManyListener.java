package sharedresources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.sun.corba.se.spi.activation.Server;

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
    private boolean isHost, flag = true;
    private Thread t = null;

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
        t = new Thread(this);
        t.start();
    }
    
    public void stop(){
    	try {
    		if(socket != null){
				socket.leaveGroup(group);
				socket.close();
				socket = null;
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	t.stop();
    	flag = false;
    	
    }
	@Override
	public void run() {
        while(flag) {
        	try {
	            byte[] incomingData = new byte[1024];
	            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
	            socket.receive(incomingPacket);
	            byte[] data = incomingPacket.getData();
	            ByteArrayInputStream in = new ByteArrayInputStream(data);
	            ObjectInputStream is = new ObjectInputStream(in);
            	Message received = (Message)is.readObject();  
            	handleMessage(received);
            } catch(ClassNotFoundException | IOException ex) {
            	ex.printStackTrace();
            	flag = false;
            }
        }

	}
	
	private void handleMessage(Message message) {
		//If you are a client then ignore all messages except form the HostIsFound
		if(!Commands.messageIsOfCommand(message, Commands.hostFound) && !isHost){
			return;
		}
		
		//Ignore your own Messages except for election messages
		if(message.getProcessID().equals(Misc.processID) 
				&& !Commands.messageIsOfCommand(message, Commands.IAmTheMaster)
				&& !Commands.messageIsOfCommand(message, Commands.startElection)
				&& !Commands.messageIsOfCommand(message, Commands.vote)){
			return;
		}
		//Messages destined for clients only
		if(message.getMessageType().equals(Message.MessageType.mClientCommand) 
				&& isHost
				&& message.getClientAsReceiver())
		{
			return;
		}
		
		//For forwarding messages to clients
		if(message.getMessageType().equals(Message.MessageType.mHostChat) && isHost) {
			message.setMessageType(Message.MessageType.hostChat); //TODO This message is not pushed in here but in oneToOneListener, or not?? :s
			long messageId = Misc.getNextMessageId();
			message.setId(messageId); //This message must have a new unique id 
			
			messageController.queueHostChat.push(message);
			
			//Put the message in a queue for possible re-sending
			addToRetryQueue(message);
			
		}
		
		//TODO explain queues and commands in report
		messageController.pushMessageInCorrectQueue(message);
		
	}


    private void addToRetryQueue(Message message) {
        ForwardMessage forwardMessage = new ForwardMessage(message, message.getId());
        for(ConnectedClient client: ConnectedClientsList.clients) {
            forwardMessage.addClient(client);
        }
        
        messageController.queueSentMessagesByHostToClient.add(forwardMessage);
        
    }
}
