package client;

import sharedresources.Message;

public class ClientToHostAckSender implements Runnable {

    
    private Client client;
    private boolean flag;

    public ClientToHostAckSender(Client client) {
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
        this.flag = true;
        while(flag){
            Message message = client.messageController.queueAcknowledgements.pop();
            if(message != null ){
                client.clientToHost.sendMessage(message);
            }   
            
            try {
                Thread.sleep(150);
            } 
            catch (InterruptedException e) { 
                e.printStackTrace();
                flag=false;
            }
        }
    }

}
