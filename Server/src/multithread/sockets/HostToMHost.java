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
import sharedresources.Message;
import sharedresources.Misc;
import sharedresources.Misc.MessageType;

/**
 * This class is used for communication between a host and multiple hosts.
 *  - Normal hosts need to be able to send a broadcast message to detect if the master is still alive.
 *  - The master needs to send a broadcast message to all the hosts to see if one host died.
 *  - The master needs to send a broadcast message to find a suitable host (including itself) for a client
 *
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
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                InetAddress group = InetAddress.getByName(Config.multiCastAddress);
                Message message;
                String command;
                if(Config.master) {
                	command = Commands.masterPing;
                } else {
                	command = Commands.hostPing;
                }
            	command = Commands.constructCommand(command);
            	message = new Message(MessageType.multipleReceivers,true,Misc.getProcessID(), "host", command);
                
                if(message != null){
                	System.out.println("Sending message: " + message.getText());
	                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	                ObjectOutputStream os = new ObjectOutputStream(outputStream);
	                os.writeObject(message);
	                byte[] data = outputStream.toByteArray();
	                DatagramPacket packet = new DatagramPacket(data, data.length, group, Config.hostMultiCastGroup);
	                socket.send(packet);
                }
                
                try {
                    Thread.sleep(Config.DELAY);
                } 
                catch (InterruptedException e) { 
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        socket.close();    
        
    }
}
