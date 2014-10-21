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

/**
 * This class is used for communication with a host and multiple clients.
 * The host broadcasts the messages in the queue to its clients 
 */
public class HostToMClient implements Runnable {

    private final int DELAY = 100;
    private DatagramSocket socket;

    public static int counter = 0;

    public HostToMClient() {
        try {
            socket = new DatagramSocket(Server.port + 1000); //TODO change to 0
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
                Message message = Server.messageControllerMClient.pop();
//                MessageController.push(message);
                
                if(message != null){
                	System.out.println("@HostToMultipleClients\n\tSending message: " + message.getText());
	                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	                ObjectOutputStream os = new ObjectOutputStream(outputStream);
	                os.writeObject(message);
	                byte[] data = outputStream.toByteArray();
	                DatagramPacket packet = new DatagramPacket(data, data.length, group, 5555);
	                socket.send(packet);
                }
                
                try {
                    Thread.sleep(DELAY);
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
