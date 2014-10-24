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
import sharedresources.Misc;

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
            try {
                InetAddress group = InetAddress.getByName(Config.multiCastAddress);
//                System.out.println("@HostToMultipleClients\n\tSending message: " + message.getText());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(message);
                byte[] data = outputStream.toByteArray();
                DatagramPacket packet = new DatagramPacket(data, data.length, group, 5555);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

}
