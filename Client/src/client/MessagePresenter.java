package client;

import sharedresources.Message;

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
				Client.knownClients.clientExists(message.getUsername());
				System.out.println("Username: " + message.getUsername());
				client.AddTextToMainPanel("<b style=\"color:"+Client.knownClients.getColor(message.getUsername())+"\">" + message.getUsername() + "</b>: " + message.getText());
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
