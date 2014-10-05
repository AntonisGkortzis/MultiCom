package multithread.sockets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import sharedresources.Config;
import sharedresources.Message;
import sharedresources.MessageController;

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
        SendStatusUpdate sendPing = new SendStatusUpdate();
        sendPing.start();
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
      
                Message message = Server.messageControllerMHost.pop();

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
                    Thread.sleep(500); //TODO Must be faster than push from ping for now
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
