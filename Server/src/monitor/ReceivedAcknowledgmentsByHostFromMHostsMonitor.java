package monitor;

import java.util.Iterator;

import multithread.sockets.Server;
import sharedresources.Commands;
import sharedresources.ForwardMessage;
import sharedresources.HostAmountSendPair;
import sharedresources.Message;

public class ReceivedAcknowledgmentsByHostFromMHostsMonitor implements Runnable {

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
			System.out.println("Acks size: " + Server.messageController.queueSentMessagesByHostToMHost.size());
			if(Server.messageController.queueSentMessagesByHostToMHost.size() <= 0 )
				continue;
						
			//check if there are any unverified messages in the SentMessages queue
			Iterator<ForwardMessage> iteratorMsg = Server.messageController.queueSentMessagesByHostToMHost.iterator();
			while(iteratorMsg.hasNext()) {
			    ForwardMessage forwardMessage = iteratorMsg.next();
			    Iterator<HostAmountSendPair> iteratorPair = forwardMessage.getHosts().iterator();
			    while(iteratorPair.hasNext()) {
			        HostAmountSendPair hostPair = iteratorPair.next();

			        hostPair.incNrOfRetries();
			        if(hostPair.getNrOfRetries()>2) { //remove host after some retries (not responding/sending acks)
                        iteratorPair.remove();
                        System.out.println("Too much retries, so remove host with PID: " + hostPair.getHost().getProcessID());
                        if(forwardMessage.getHosts().size()<=0) { //no hosts anymore so this message is done
                            iteratorMsg.remove();
                            System.out.println("No hosts anymore acknowledging, so remove ForwardMsg and give up resending it");
                            continue;
                        }
                        continue;
                    }
                    
                    Message message = forwardMessage.getMessage();
                    //adding the message to the Send queue for immediate re-sending
                    Server.messageController.queueSend.push(message);
                    
                    //TODO Are the following necessary for this functionality????
                    /*
                    //Set the message text as the first time it is a raw message, but the second time a command
                    String messageText = message.getText();
                    if(Commands.messageIsOfCommand(message, Commands.targetedResentMessage)) {
                        messageText = Commands.getTextParseTargetedMessageText(message);
                    }
                    String command = Commands.constructCommand(Commands.targetedResentMessage, hostPair.getHost().getProcessID(), messageText);
                    message.setCommand(true);
                    message.setText(command);
                    Server.messageController.queueHostChat.push(message);
                    */
                    try {
                        Thread.sleep(500); //!!!! bigger than popper delay of HostToMClient
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
//                    System.out.println("@ReceivedAcksByHostsMonitor = Message sent " + message + " to pid: " + clientPair.getClient().getProcessID());
			    }
			}

		}

	}

}
