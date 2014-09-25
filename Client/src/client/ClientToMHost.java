package client;

import java.net.DatagramSocket;

/**
 * This class is used when starting a client which wants to connect to a host.
 * A message is send to all the available hosts through MultiCast.
 * 
 * The response will be a host to which the client can connect implemented in MClientListener.
 *
 */
public class ClientToMHost {
	
	private DatagramSocket socket;

	public ClientToMHost(Client client) {
//        try {
//            socket = new DatagramSocket(Server.port + 1000);
//        } catch (SocketException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
	}
	

	
}
