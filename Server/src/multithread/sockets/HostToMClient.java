package multithread.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import sharedresources.Message;
import sharedresources.MessageQueue;

/**
 * Multicasts the messages in the queue to the clients 
 */
public class HostToMClient implements Runnable {

    private final int DELAY = 1000;
//    List<String> messageList = new ArrayList<>();
    MessageQueue messageQueue = new MessageQueue();
    private DatagramSocket socket;
    private String mcAddress = "224.0.0.10";

    public HostToMClient() {
        Message message = new Message(false, true, true, false, "username", " test 1");
        Message message2 = new Message(false, true, true, false, "username2", " test 2");

        messageQueue.push(message);
        messageQueue.push(message2);
    }
    
    public void start() {
        try {
            socket = new DatagramSocket(Server.port + 1000);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buf = new byte[256];
                // don't wait for request...just send a quote

                Message message = messageQueue.pop();
                messageQueue.push(message);
                InetAddress group = InetAddress.getByName(mcAddress);
                buf = message.getText().getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 5555);
                socket.send(packet);

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
