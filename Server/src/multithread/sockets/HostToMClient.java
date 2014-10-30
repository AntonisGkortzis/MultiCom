package multithread.sockets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.ConnectedClient;
import sharedresources.ConnectedClientsList;
import sharedresources.ForwardMessage;
import sharedresources.Message;

/**
 * This class is used for communication with a host and multiple clients.
 * The host broadcasts the messages in the queue to its clients 
 */
public class HostToMClient implements Runnable {

    private final int DELAY = 250;
    private DatagramSocket socket;

    public static int counter = 0;

    public HostToMClient() {
        try {
            socket = new DatagramSocket(); //port is random
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag) {                
            Message message = Server.messageController.queueHostChat.pop();
            
            flag = sendMessage(message);
            try {
                Thread.sleep(DELAY);
            } 
            catch (InterruptedException e) { 
                e.printStackTrace();
                flag = false;
            }
        }
        
        socket.close();        
    }
    
    private boolean sendMessage(Message message) {
        if(message != null){     
        	if(Commands.messageIsOfCommand(message, Commands.forwardMessage)){
        		message.setText(Commands.getParseMessageText(message));
        		System.out.println("@@ Formatted (forwarded) message " + message.toString());
        	}
        	
        	System.out.println("Sending L:  " + message);
            try {
                InetAddress group = InetAddress.getByName(Config.multiCastAddress);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(message);
                byte[] data = outputStream.toByteArray();
                DatagramPacket packet = new DatagramPacket(data, data.length, group, Server.port + 1); //TODO fix this "hard coded" port
                socket.send(packet);
                
    			//Put the chat message in a queue for possible re-sending
    			if(!message.isCommand()) addToRetryQueue(message);
    			
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    private void addToRetryQueue(Message message) {
        ForwardMessage forwardMessage = new ForwardMessage(message, message.getId());
        System.out.println("@@ HostToMClient adding forwarded message " + forwardMessage.getMessage().toString());
        Server.messageController.queueSentMessagesByHostToClient.add(forwardMessage);
        
    }
}
