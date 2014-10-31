package client;

import java.util.Iterator;

import sharedresources.Message;
import sharedresources.Misc;


public class ReceivedAcknowledgmentsByClientMonitor implements Runnable {
	
	private Client client;
    private boolean flag;

	public ReceivedAcknowledgmentsByClientMonitor(Client client) {
		this.client = client;
	}
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public void stop() {
	    this.flag = false;
	}
	@Override
	public void run() {
		flag = true;
		while(flag) {
			if(client.messageController.queueSentMessagesByClient.size() <= 0 ){
			    try { //To stop the message from being directly resent
			        Thread.sleep(2000);
			    } catch (InterruptedException e) {
			        e.printStackTrace();
			        flag=false;
			    }
				continue;
			}
			try { //Delay between resent
			    Thread.sleep(2000);//TODO set a fixed delay in Config
			} 
			catch (InterruptedException e) { 
			    e.printStackTrace();
			    flag = false;
			}
			
			//check if there are any unverified messages in the SentMessages queue
			Iterator<Message> iterator = client.messageController.queueSentMessagesByClient.iterator();
			while(iterator.hasNext()) {
			    Message message = iterator.next();
	             //Remove the message if it has been re-sent more than 2 times
                if(message.getTimesSent() > 2){
//                    client.messageController.queueSentMessagesByClient.remove(Misc.processID, message.getId());
                    iterator.remove();
                } else {
//                  System.out.println("@@ RecAckMonitorClient: resending msg, nrOfTimes: " + message.getTimesSent());
                    flag = client.clientToHost.sendMessage(message);
                    
                }
			}
//			for(int i=0; i<client.messageController.queueSentMessagesByClient.size(); i++) {
//				Message message = client.messageController.queueSentMessagesByClient.get(i);
//				//Remove the message if it has been re-sent more than 2 times
//				if(message.getTimesSent() > 2){
//					client.messageController.queueSentMessagesByClient.remove(Misc.processID, message.getId());
//				} else {
////				    System.out.println("@@ RecAckMonitorClient: resending msg, nrOfTimes: " + message.getTimesSent());
//				    flag = client.clientToHost.sendMessage(message);
//					
//				}
//			}
          	
		}
	}

}
