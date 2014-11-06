package client;

import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;

public class ClientHeartbeatToHost implements Runnable {

    
    private Client client;
    private boolean flag;
	private Thread t;
    
    public ClientHeartbeatToHost(Client client) {
        this.client = client;
    }
    
    public void start() {
        this.t = new Thread(this);
        t.start();
    }
    
    public void stop() {
        this.flag = false;
//        t.interrupt();
    }
    
    @Override
    public void run() {
        this.flag = true;
        String command = Commands.constructCommand(Commands.clientHeartbeat);
        Message message = new Message(Message.MessageType.clientCommand, command);
        while(!Thread.interrupted() && flag) {
            if(!client.isConnected) return;
            try {
                Thread.sleep(Config.clientHeartbeatDelay); //Must be lower than the declareDead in ClientMonitor of server
            } catch (InterruptedException e) {
                flag = false;
            }
            client.clientToHost.sendMessage(message);
        }
    }
}
