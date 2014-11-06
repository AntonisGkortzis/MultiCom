package client;

import sharedresources.Message;
import sharedresources.Misc;

public class MessagePresenter implements Runnable {
	private Client client;
	private boolean flag = true;
	
	public MessagePresenter(Client client){
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
		while(flag){
			Message message = client.messageController.queueClientReceivedMessages.pop();
			
			
			if(message != null){
				Client.knownClients.clientExists(message.getOriginalSendersProcessID());
				//The name presented on the Messenger will have a format of originalSender'sProcessId | username
				String username = message.getOriginalSendersProcessID() + " | " + message.getUsername();
				
				//If you are the sender and receiver of this message the name presented in the Messenger is "You"
				if(message.getOriginalSendersProcessID().equals(Misc.processID)){
					username = "You";
				}
				//present the name with a unique color
				client.AddTextToMainPanel("<b style=\"color:" + Client.knownClients.getColor(message.getOriginalSendersProcessID()) + "\">" + username + "</b><br/>" + message.getText());
			}
			
			try {
				Thread.sleep(150);
			} catch (InterruptedException ex){
				ex.printStackTrace();
				flag = false;
			}
		}
				
		
	}

}
