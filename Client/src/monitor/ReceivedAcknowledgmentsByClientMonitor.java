package monitor;

import java.util.Date;
import java.util.Iterator;

import client.Client;
import sharedresources.Message;


public class ReceivedAcknowledgmentsByClientMonitor implements Runnable {
	
	private Client client;
    private boolean flag;
    private long timeToWait = 2000;

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
			try { //Delay between resent
			    Thread.sleep(200);
			} 
			catch (InterruptedException e) { 
			    e.printStackTrace();
			    flag = false;
			}
			
			//check if there are any unverified messages in the SentMessages queue
			Iterator<Message> iterator = client.messageController.queueSentMessagesByClient.iterator();
			while(iterator.hasNext()) {
			    Message message = iterator.next();
			    long currentTime = new Date().getTime();
			    if(currentTime-message.getTimeSent() > this.timeToWait) {
			    	message.setTimeSent(currentTime);
			    	//Remove the message if it has been re-sent more than 2 times
	                if(message.getTimesSent() > 2){
	                    iterator.remove();
	                } else {
	                    flag = client.clientToHost.sendMessage(message);
	                }
			    }
			}
		}
	}

}
