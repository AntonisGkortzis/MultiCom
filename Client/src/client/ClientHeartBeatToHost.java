package client;

import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;

public class ClientHeartBeatToHost implements Runnable {

    
    private Client client;
    private boolean flag;
    
    public ClientHeartBeatToHost(Client client) {
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
        String command = Commands.constructCommand(Commands.clientHeartBeat);
        Message message = new Message(Message.MessageType.clientCommand, true, command);
        while(flag) {
            if(!client.isConnected) return;
            try {
                Thread.sleep(Config.clientHeartbeatDelay); //Must be lower than the declareDead in ClientMonitor of server
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            client.clientToHost.sendMessage(message);
            
        }
        

    }

}
