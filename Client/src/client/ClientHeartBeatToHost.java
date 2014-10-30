package client;

import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;

public class ClientHeartBeatToHost implements Runnable {

    
    private ClientToHost clientToHost;
    public ClientHeartBeatToHost(ClientToHost clientToHost) {
        this.clientToHost = clientToHost;
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        boolean flag = true;
        String command = Commands.constructCommand(Commands.clientHeartBeat);
        Message message = new Message(Message.MessageType.clientCommand, true, command);
        while(flag) {
            try {
                Thread.sleep(Config.clientHeartbeatDelay); //Must be lower than the declareDead in ClientMonitor of server
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            clientToHost.sendMessage(message);
            
        }
        

    }

}
