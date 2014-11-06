package multithread.sockets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

import sharedresources.Commands;
import sharedresources.Config;
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
        	}
        	
            try {
                InetAddress group = InetAddress.getByName(Config.multiCastAddress);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(message);
                byte[] data = outputStream.toByteArray();
                DatagramPacket packet = new DatagramPacket(data, data.length, group, Server.port );
                socket.send(packet);
                
    			//Put the chat message in a queue for possible re-sending
    			if(!Commands.messageIsOfCommand(message, Commands.targetedResentMessage)) addToRetryQueue(message);
    			
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    private void addToRetryQueue(Message message) {
        ForwardMessage forwardMessage = new ForwardMessage(message, message.getId(), false);
        message.setTimeSent(new Date().getTime());
        Server.messageController.queueSentMessagesByHostToClient.add(forwardMessage);
    }
}
