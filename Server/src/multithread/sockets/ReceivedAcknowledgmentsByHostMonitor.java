package multithread.sockets;

import java.util.Iterator;

import sharedresources.ClientAmountSendPair;
import sharedresources.Commands;
import sharedresources.ForwardMessage;
import sharedresources.Message;

public class ReceivedAcknowledgmentsByHostMonitor implements Runnable {
	
	public ReceivedAcknowledgmentsByHostMonitor(){}
	
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
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
			
			System.out.println("@@ ReceivedAcksByHostsMonitor sentMessagesSize: " + Server.messageController.queueSentMessagesByHostToClient.size());
			
			//check if there are any unverified messages in the SentMessages queue
			Iterator<ForwardMessage> iteratorMsg = Server.messageController.queueSentMessagesByHostToClient.iterator();
			while(iteratorMsg.hasNext()) {
			    ForwardMessage forwardMessage = iteratorMsg.next();
			    Iterator<ClientAmountSendPair> iteratorPair = forwardMessage.getClients().iterator();
			    while(iteratorPair.hasNext()) {
			        ClientAmountSendPair clientPair = iteratorPair.next();
			        if(clientPair.getNrOfRetries()>2) { //remove client after some retries (not responding/sending acks)
                        iteratorPair.remove();
                        System.out.println("Too much retries, so remove client");
                        if(forwardMessage.getClients().size()<=0) { //no clients anymore so this message is done
                            iteratorMsg.remove();
                            System.out.println("No clients anymore, so remove ForwardMsg");
                            continue;
                        }
                        continue;
                    }
                    
                    Message message = forwardMessage.getMessage();
                    
                    //Set the message text as the first time it is a raw message, but the second time a command
                    String messageText = message.getText();
                    if(Commands.messageIsOfCommand(message, Commands.targetedResentMessage)) {
                        messageText = Commands.getTextParseTargetedMessageText(message);
                    }
                    String command = Commands.constructCommand(Commands.targetedResentMessage, clientPair.getClient().getProcessID(), messageText);
                    message.setCommand(true);
                    message.setText(command);
                    Server.messageController.queueHostChat.push(message);
                    clientPair.incNrOfRetries();

                    System.out.println("@ReceivedAcksByHostsMonitor = Message sent " + forwardMessage.getMessage().getMessageType());
			    }
			}

		}
	}
//
//	private boolean sendMessage(Socket socket, Message message) {
//    	if (socket == null) {
//			System.out.println("@ReceivedAcksByHostsMonitor Not connected to client");
//			return false;
//		}
//    	try {
//    		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//    		outputStream.writeObject(message);
//    		outputStream.flush();
//    	} catch(IOException ex) {
//            ex.printStackTrace();
//            return false;
//    	}
//    	
//    	return true;
//    }
		
}

