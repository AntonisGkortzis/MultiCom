package client;

import sharedresources.Message;


public class ReceivedAcknowledgmentsByClientMonitor implements Runnable {
	
	private ClientToHost clientToHost;

	public ReceivedAcknowledgmentsByClientMonitor(ClientToHost clientToHost) {
		this.clientToHost = clientToHost;
	}
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

			if(Client.messageController.queueSentMessagesByClient.size() <= 0 ){
				flag = false;
				continue;
			}
			
			//check if there are any unverified messages in the SentMessages queue
			for(int i=0; i<Client.messageController.queueSentMessagesByClient.size(); i++) {
				Message message = Client.messageController.queueSentMessagesByClient.get(i);
				//Remove the message if it has been re-sent more than 2 times
				if(message.getTimesSent() > 2){
					Client.messageController.queueSentMessagesByClient.remove(message.getUsername(), message.getId());
				} else {
					flag = clientToHost.sendMessage(message);
				}
			}
          	
		}
	}

}
