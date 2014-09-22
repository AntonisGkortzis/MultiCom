package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MClientListener implements Runnable {
    
    private InetAddress group;
    private MulticastSocket socket;
    private Client client;
    private String mcAddress = "224.0.0.10";
    public MClientListener(Client client) {
        this.client = client;
        try {
            socket = new MulticastSocket(5555);
            group = InetAddress.getByName(mcAddress);
            socket.joinGroup(group);
        } catch (IOException e) {
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
            DatagramPacket packet;
            while(true) {
                System.out.println("Receiving....");
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                System.out.println("Received packet");

                String received = new String(packet.getData());
                System.out.println("Msg received: " + received);
                client.AddTextToMainPanel(received);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        socket.close();
        
    }
}
