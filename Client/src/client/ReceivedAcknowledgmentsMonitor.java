package client;

import sharedresources.Message;


public class ReceivedAcknowledgmentsMonitor implements Runnable {
	
	private ClientToHost clientToHost;

	public ReceivedAcknowledgmentsMonitor(ClientToHost clientToHost) {
		this.clientToHost = clientToHost;
	}
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
				//Remove the message if it has been re-sent more than 2 times
				if(message.getTimesSent() > 2){
					Client.messageController.queueSentMessages.remove(message.getUsername(), message.getId());
				} else {
					clientToHost.sendMessage(message);
				}
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
