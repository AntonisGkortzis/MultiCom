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
import sharedresources.MessageQueue;

/**
 * Multicasts the messages in the queue to the clients 
 */
public class HostToMClient implements Runnable {

    private final int DELAY = 100;
//    List<String> messageList = new ArrayList<>();
    //MessageQueue messageQueue;
    private DatagramSocket socket;
//    private String mcAddress = "192.168.1.255";
    private String mcAddress = "0.0.0.0";
    public static int counter = 0;

    public HostToMClient() {
        Message message = new Message(false, true, true, false, "username", " test ");
        Message message2 = new Message(false, true, true, false, "username2", " test 2");
        
        Server.getQueue().push(message);
        Server.getQueue().push(message2);
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

                //Message message = Server.getQueue().pop();
                //message.setText(message.getText() + counter++);
                
                InetAddress group = InetAddress.getByName(mcAddress);
//                buf = message.getText().getBytes();
//                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 5555);
                /* sending objects instead of strings START */
                Message message = Server.getQueue().pop();
                //Server.getQueue().push(message);
                if(message != null){
                	System.out.println("Sending message: " + message.getText());
	                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	                ObjectOutputStream os = new ObjectOutputStream(outputStream);
	                os.writeObject(message);
	                byte[] data = outputStream.toByteArray();
	                DatagramPacket packet = new DatagramPacket(data, data.length, group, 5555);
	                /* sending objects instead of strings END */
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
