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
                System.out.println("@ Client to HostACK Sending acknowledgment " + message.toString());
                client.clientToHost.sendMessage(message);
            }   
            
            try {
                Thread.sleep(150); //TODO put delay in config. Must be faster than push from ping for now
            } 
            catch (InterruptedException e) { 
                e.printStackTrace();
                flag=false;
            }
            
        }

    }

}
