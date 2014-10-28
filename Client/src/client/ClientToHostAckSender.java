package client;

import java.net.Socket;

import sharedresources.Message;

public class ClientToHostAckSender implements Runnable {

    
    private ClientToHost clientToHost;

    public ClientToHostAckSender(ClientToHost clientToHost) {
       this.clientToHost = clientToHost;
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    
    @Override
    public void run() {
        boolean flag = true;
        while(flag){
            Message message = Client.messageController.queueAcknowledgements.pop();
            if(message != null ){
                System.out.println("@ Client to HostACK Sending acknowledgment " + message.toString());
                clientToHost.sendMessage(message);
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
