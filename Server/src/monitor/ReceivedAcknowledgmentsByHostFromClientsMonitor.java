package monitor;

import java.util.Date;
import java.util.Iterator;

import multithread.sockets.Server;
import sharedresources.ClientAmountSendPair;
import sharedresources.Commands;
import sharedresources.ForwardMessage;
import sharedresources.Message;

public class ReceivedAcknowledgmentsByHostFromClientsMonitor implements Runnable {
	
	private final int timeToWait = 2000;
	
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		boolean flag = true;
		while(flag) {
			try {
				Thread.sleep(200);//TODO set a fixed delay in Config
			}
			catch (InterruptedException e) { 
				e.printStackTrace();
				flag = false;
			}
//			System.out.println("Acks size: " + Server.messageController.queueSentMessagesByHostToClient.size());
			if(Server.messageController.queueSentMessagesByHostToClient.size() <= 0 )
				continue;
			
//			System.out.println("@@ ReceivedAcksByHostsMonitor sentMessagesSize: " + Server.messageController.queueSentMessagesByHostToClient.size());
			
			//check if there are any unverified messages in the SentMessages queue
			Iterator<ForwardMessage> iteratorMsg = Server.messageController.queueSentMessagesByHostToClient.iterator();
			while(iteratorMsg.hasNext()) {
			    ForwardMessage forwardMessage = iteratorMsg.next();
			    Iterator<ClientAmountSendPair> iteratorPair = forwardMessage.getClients().iterator();

			    long currentTime = new Date().getTime();
			    Message message = forwardMessage.getMessage();
			    if(currentTime - forwardMessage.getMessage().getTimeSent() > this.timeToWait) {
			    	message.setTimeSent(currentTime);
				    while(iteratorPair.hasNext()) {
				        ClientAmountSendPair clientPair = iteratorPair.next();
					    clientPair.incNrOfRetries();
				        if(clientPair.getNrOfRetries()>2) { //remove client after some retries (not responding/sending acks)
	                        iteratorPair.remove();
	                        System.out.println("Too much retries, so remove client with PID: " + clientPair.getClient().getProcessID());
	                        if(forwardMessage.getClients().size()<=0) { //no clients anymore so this message is done
	                            iteratorMsg.remove();
	                            System.out.println("No clients anymore acknowledgeing, so remove ForwardMsg and give up resending it");
	                            continue;
	                        }
	                        continue;
				        }
	                    
	                    //Set the message text as the first time it is a raw message, but the second time a command
	                    String messageText = message.getText();
	                    if(Commands.messageIsOfCommand(message, Commands.targetedResentMessage)) {
	                        messageText = Commands.getTextParseTargetedMessageText(message);
	                    }
	                    String command = Commands.constructCommand(Commands.targetedResentMessage, clientPair.getClient().getProcessID(), messageText);
	                    message.setText(command);
	                    Server.messageController.queueHostChat.push(message); //TODO in holdback queue??
	                    
	                    try {
	                        Thread.sleep(500); //!!!! bigger than popper delay of HostToMClient
	                    } catch (InterruptedException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
			        }
//                    System.out.println("@ReceivedAcksByHostsMonitor = Message sent " + message + " to pid: " + clientPair.getClient().getProcessID());
			    }
			}
		}
	}
}

