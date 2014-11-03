package monitor;

import java.util.Date;

import client.Client;
import sharedresources.Config;
import sharedresources.Message;

public class HoldbackQueueMonitor implements Runnable {

    boolean flag;
    Client client;
    
    public HoldbackQueueMonitor(Client client) {
        this.client = client;
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    
    public void stop() {
        flag=false;
    }

    /**
     * Handle every message still in the holdback queue
     */
    public void unload() {
        if(!this.client.holdbackQueue.isEmpty()) {
        	System.out.println("Unloading all the messages from my holdback queue");
        }
        while(!this.client.holdbackQueue.isEmpty()) {
            this.handleMessage(this.client.holdbackQueue.poll());
        }
    }
    
    @Override
    public void run() {
        flag=true;
        while(flag) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                flag=false;
                e.printStackTrace();
            }
            long currentTime = new Date().getTime();
            while(!this.client.holdbackQueue.isEmpty()) {
                if(currentTime - this.client.holdbackQueue.peek().getTimeReceived() > Config.holdBackQueueDelay) {
                    //The message is ready to be presented.
                    Message message = this.client.holdbackQueue.poll(); //pop message
                    
                    //Prepare message for present queue
                    this.handleMessage(message);
                } else { //First message is not ready
                    break; 
                }
            }
        }
    }
    
    private void handleMessage(Message message) {
        this.client.messageController.queueClientReceivedMessages.push(message);
    }
}

