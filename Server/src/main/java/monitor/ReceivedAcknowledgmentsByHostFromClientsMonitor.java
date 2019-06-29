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
				Thread.sleep(200);
			}
			catch (InterruptedException e) { 
				e.printStackTrace();
				flag = false;
			}
			if(Server.messageController.queueSentMessagesByHostToClient.size() <= 0 )
				continue;
			
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
				        if(clientPair.getNrOfRetries()>2) { //remove client after some retries (not responding/sending acks)
	                        iteratorPair.remove();
	                        System.out.println("%ACK%-- Stop sending the message to Messenger " + clientPair.getClient().getProcessID() + 
	                        		" because of too many retries --%ACK%");
	                        if(forwardMessage.getClients().size()<=0) { //no clients anymore so this message is done
	                            iteratorMsg.remove();
	                            System.out.println("%ACK%-- No Messengers anymore to resent this message to, so give up resending it --%ACK%");
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
	                    Server.messageController.queueHostChat.push(message);
	                    clientPair.incNrOfRetries();
	                    
	                    try {
	                        Thread.sleep(200); //!!!! bigger than popper delay of HostToMClient
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
			        }
			    }
			}
		}
	}
}
