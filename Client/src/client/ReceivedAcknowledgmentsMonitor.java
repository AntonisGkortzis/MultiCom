package client;

import sharedresources.Message;


public class ReceivedAcknowledgmentsMonitor implements Runnable {
	
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		while(true) {
			if(Client.messageController.queueSentMessages.size() <= 0 )
				continue;
			
			//check if there are any unverified messages in the SentMessages queue
			for(int i=0; i<Client.messageController.queueSentMessages.size(); i++) {
				Message message = Client.messageController.queueSentMessages.get(i);
				ClientToHost.sendMessage(message);
			}
          	
            try {
                Thread.sleep(2000);//TODO set a fixed delay in Config
            } 
            catch (InterruptedException e) { 
                e.printStackTrace();
            }
		}
	}

}
