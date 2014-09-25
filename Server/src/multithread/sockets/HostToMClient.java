package multithread.sockets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import sharedresources.Message;
import sharedresources.MessageController;
import sharedresources.MessageQueue;

/**
 * Multicasts the messages in the queue to the clients 
 */
public class HostToMClient implements Runnable {

    private final int DELAY = 100;
    private DatagramSocket socket;
//    private String mcAddress = "192.168.1.255";
    private String mcAddress = "0.0.0.0";
    public static int counter = 0;

    public HostToMClient() {
        Message message = new Message(false, true, true, false, "username", " test ");
        Message message2 = new Message(false, true, true, false, "username2", " test 2");
        
//        MessageController.push(message);
//        Message temp = MessageController.pop();
//        System.out.println("Temp: " + temp.getText());
//        MessageController.push(message2);
        
        try {
            socket = new DatagramSocket(Server.port + 1000);
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
                byte[] buf = new byte[256];
                
                InetAddress group = InetAddress.getByName(mcAddress);
                Message message = MessageController.pop();
//                MessageController.push(message);
                
                if(message != null){
                	System.out.println("Sending message: " + message.getText());
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
