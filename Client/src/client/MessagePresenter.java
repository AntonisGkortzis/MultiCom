package client;

import sharedresources.Message;

public class MessagePresenter implements Runnable {
	private Client client;
	
	public MessagePresenter(Client client){
		this.client = client;
	}
	
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		boolean flag = true;
		while(flag){
			Message message = Client.messageController.queueClientReceivedMessages.pop();
			
			if(message != null){
				client.AddTextToMainPanel(message.getTimestamp() + "|" + message.getUsername() + ": " + message.getText());
			}
			
			try{
				Thread.sleep(150); // TODO put in Config
			} catch (InterruptedException ex){
				ex.printStackTrace();
				flag = false;
			}
		}
				
		
	}

}
