package client;

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
import sharedresources.Misc.MessageType;

/**
 * This class is used when starting a client which wants to connect to a host.
 * A message is send to all the available hosts through MultiCast.
 * 
 * The response will be a host to which the client can connect implemented in MClientListener.
 *
 */
public class ClientToMHost {
	
	private DatagramSocket socket;
	private Client client;

	public ClientToMHost(Client client) {
		this.client = client;
		
        try {
            socket = new DatagramSocket(0);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
   	}

	public void sendConnectRequest(){
		InetAddress group;
		try {
			group = InetAddress.getByName(Config.multiCastAddress);
			
			String command = Commands.constructCommand(Commands.connectRequest);
	        Message message = new Message(MessageType.mHostCommand, true, client.getUserName(), command);
	    	System.out.println("Message " + message.getText());
	    	
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        ObjectOutputStream os = new ObjectOutputStream(outputStream);
	        os.writeObject(message);
	        byte[] data = outputStream.toByteArray();
	        DatagramPacket packet = new DatagramPacket(data, data.length, group, Config.hostMultiCastGroup);
	        socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
