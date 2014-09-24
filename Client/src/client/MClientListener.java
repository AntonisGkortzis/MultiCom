package client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import sharedresources.Message;

public class MClientListener implements Runnable {
    
    private InetAddress group;
    private MulticastSocket socket;
    private Client client;
//    private String mcAddress = "192.168.1.255";
    private String mcAddress = "0.0.0.0";
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
                byte[] incomingData = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try{
                	Message received = (Message)is.readObject();  
                	System.out.println("Client received: " + received.getText());
                	client.AddTextToMainPanel(received.getText());
                } catch(ClassNotFoundException ex) {
                	ex.printStackTrace();
                }

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
