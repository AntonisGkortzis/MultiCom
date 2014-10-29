package multithread.sockets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import sharedresources.ClientAmountSendPair;
import sharedresources.Commands;
import sharedresources.ConnectedClient;
import sharedresources.ForwardMessage;
import sharedresources.Message;

public class ReceivedAcknowledgmentsByHostMonitor implements Runnable {
	
	public ReceivedAcknowledgmentsByHostMonitor(){
		System.out.println("ReceivedAcknowledgmentsByHostMonitor initialized");
	}
	
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		System.out.println("ReceivedAcknowledgmentsByHostMonitor run method started");
		boolean flag = true;
		while(flag) {
			try {
				Thread.sleep(2000);//TODO set a fixed delay in Config
			}
			catch (InterruptedException e) { 
				e.printStackTrace();
				flag = false;
			}
//			System.out.println("Acks size: " + Server.messageController.queueSentMessagesByHostToClient.size());
			if(Server.messageController.queueSentMessagesByHostToClient.size() <= 0 )
				continue;
			
			//check if there are any unverified messages in the SentMessages queue
			for(int i=0; i<Server.messageController.queueSentMessagesByHostToClient.size(); i++) {
				ForwardMessage forwardMessage = Server.messageController.queueSentMessagesByHostToClient.get(i);
				for(ClientAmountSendPair clientPair : forwardMessage.getClients()){
//					flag = sendMessage(clientPair.getClient().getSocket(), forwardMessage.getMessage());
					Message message = forwardMessage.getMessage();
					String command = Commands.constructCommand(Commands.targetedResentMessage, clientPair.getClient().getProcessID(), message.getText());
					message.setCommand(true);
					message.setText(command);
					Server.messageController.queueHostChat.push(message);
					System.out.println("@ReceivedAcksByHostsMonitor = Message sent " + forwardMessage.getMessage().getMessageType());
				}
			}

		}
	}

	private boolean sendMessage(Socket socket, Message message) {
    	if (socket == null) {
			System.out.println("@ReceivedAcksByHostsMonitor Not connected to client");
			return false;
		}
    	try {
    		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
    		outputStream.writeObject(message);
    		outputStream.flush();
    	} catch(IOException ex) {
            ex.printStackTrace();
            return false;
    	}
    	
    	return true;
    }
		
}

