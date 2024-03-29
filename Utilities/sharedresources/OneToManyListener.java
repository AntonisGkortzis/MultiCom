package sharedresources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;
import java.util.Iterator;

import com.sun.corba.se.spi.activation.Server;

import utils.CRC32Calculator;

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
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		//If you are a client then ignore all messages except from the HostIsFound
		if(!Commands.messageIsOfCommand(message, Commands.hostFound) 
				&& !Commands.messageIsOfCommand(message, Commands.connectRequest) 
				&& !isHost){
			return;
		}
		
		//Ignore your own Messages except for election messages
		if(message.getProcessID().equals(Misc.processID) 
				&& !Commands.messageIsOfCommand(message, Commands.IAmTheMaster)
				&& !Commands.messageIsOfCommand(message, Commands.startElection)
				&& !Commands.messageIsOfCommand(message, Commands.vote)
				&& !Commands.messageIsOfCommand(message, Commands.loadBalance)){
			return;
		}
		//Messages destined for clients only
		if(message.getMessageType().equals(Message.MessageType.mClientCommand) 
				&& isHost
				&& message.getClientAsReceiver())
		{
			return;
		}
		//message is an acknowledgment and remove the HostAmountSendPair from the hosts of the forward message
		if(message.getMessageType().equals(Message.MessageType.acknowledgement) && isHost){
		    removeResendMessageToHost(message);
		}
		
		//if this message is a targeted retry message and you are not the target the ignore it
		if(Commands.messageIsOfCommand(message, Commands.targetedResentMessage) 
				&& isHost 
				&& !Commands.getStarterProcessID(message).equals(Misc.processID)){
			return;
		}
		else if(Commands.messageIsOfCommand(message, Commands.targetedResentMessage) 
				&& isHost 
				&& Commands.getStarterProcessID(message).equals(Misc.processID)){
			String command = Commands.constructCommand(Commands.forwardMessage, Commands.getTextParseTargetedMessageText(message));
			message.setText(command);
			message.setMessageType(Message.MessageType.mHostChat);
			messageController.queueHostChat.push(message);
			return;
		}
			
		
		//For forwarding messages to clients
		if(message.getMessageType().equals(Message.MessageType.mHostChat) 
				&& isHost 
				&& Commands.messageIsOfCommand(message, Commands.forwardMessage)
				&& message.getChecksum() == CRC32Calculator.getChecksum(message.getText())) {		    
		    //Creating acknowledgment for the received mHostChat message
		    String command = Commands.constructCommand(Commands.acknowledgement, message.getProcessID(), Long.toString(message.getId()));
		    Message ack = new Message(Message.MessageType.acknowledgement, command);
		    
		    //TODO DEMO: Test Host Retries
		    //Adding acknowledgment to the Send queue for immediate sending
		    messageController.queueSend.push(ack);
		    
            //Hold back queue
            message.setTimeReceived(new Date().getTime());
            HostsList.addHoldBackMessage(message);
            
            //Message is hold back so do not push it in a queue yet
		    return;
		}
		
//		System.out.println("********** Received message: " + message.toString());
		messageController.pushMessageInCorrectQueue(message);
	}
	
	/**
     * Remove the client for a message as the client has sent an acknowledgement.
     * @param message
     */
    private void removeResendMessageToHost(Message message) {
    	Iterator<ForwardMessage> iterator = messageController.queueSentMessagesByHostToMHost.iterator();
        while(iterator.hasNext()) {
        	ForwardMessage forwardMessage = iterator.next();
            if(forwardMessage.getId() == Commands.getOriginalIdOfHostForwardMessage(message)) { //correct message
                if(forwardMessage.removeHost(message.getProcessID())) {
                	iterator.remove();
                }
                break;
            }
        }
    }

}
