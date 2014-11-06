package monitor;

import java.util.Date;
import java.util.Iterator;

import multithread.sockets.Server;
import sharedresources.ForwardMessage;
import sharedresources.HostAmountSendPair;
import sharedresources.Message;

public class ReceivedAcknowledgmentsByHostFromMHostsMonitor implements Runnable {

	
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
			if(Server.messageController.queueSentMessagesByHostToMHost.size() <= 0 )
				continue;
						
			//check if there are any unverified messages in the SentMessages queue
			Iterator<ForwardMessage> iteratorMsg = Server.messageController.queueSentMessagesByHostToMHost.iterator();
			while(iteratorMsg.hasNext()) {
			    ForwardMessage forwardMessage = iteratorMsg.next();
			    Iterator<HostAmountSendPair> iteratorPair = forwardMessage.getHosts().iterator();
			    
			    long currentTime = new Date().getTime();
			    Message message = forwardMessage.getMessage();
			    if(currentTime - forwardMessage.getMessage().getTimeSent() > this.timeToWait) {
			    	message.setTimeSent(currentTime);
				    while(iteratorPair.hasNext()) {
				        HostAmountSendPair hostPair = iteratorPair.next();
	
				        hostPair.incNrOfRetries();
				        if(hostPair.getNrOfRetries()>2) { //remove host after some retries (not responding/sending acks)
	                        iteratorPair.remove();
	                        System.out.println("%ACK%-- Stop sending the message to Host " + hostPair.getHost().getProcessID() + 
	                        		" because of too many retries --%ACK%");
	                        if(forwardMessage.getHosts().size()<=0) { //no hosts anymore so this message is done
	                            iteratorMsg.remove();
	                            System.out.println("%ACK%-- No Hosts anymore to resent this message to, so give up resending it --%ACK%");
	                            continue;
	                        }
	                        continue;
	                    }
	                    
	                    //adding the message to the Send queue for immediate re-sending
	                    Server.messageController.queueSend.push(message);
	                    
	                    try {
	                        Thread.sleep(500); //!!!! bigger than popper delay of HostToMHost
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
				    }
			    }
			}
		}
	}
	
}
