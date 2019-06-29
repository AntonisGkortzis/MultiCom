package monitor;

import java.util.Date;

import multithread.sockets.Server;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Host;
import sharedresources.HostsList;
import sharedresources.Message;
import sharedresources.Misc;

public class HoldbackQueueMonitorFromHost implements Runnable {

    boolean flag;
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    
    public void stop() {
        flag=false;
    }
    
    @Override
    public void run() {
        flag = true;
        while(flag) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long currentTime = new Date().getTime();
            for(Host host: HostsList.getHostsList()) { //Go through all holdback queues
                while(!host.holdbackQueue.isEmpty()) { //handle all messages of 1 client
                    if(host.holdbackQueue.peek()!= null 
                            && currentTime - host.holdbackQueue.peek().getTimeReceived() > Config.holdBackQueueDelay) 
                    {
                        //The message is ready to be delivered.
                        Message message = host.holdbackQueue.poll(); //pop message
                        
                        //Prepare message for delivery queues
                        addMessageToDeliveryQueue(message);
                    } else { //First message is not ready
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Store the message in the delivery queue to send it to
     * the clients of the host
     * @param message
     */
    private void addMessageToDeliveryQueue(Message message) {
        //Must be placed after ack as changing the message id messes up the ack above
        message.setMessageType(Message.MessageType.hostChat);
        long messageId = Misc.getNextMessageId();
        message.setProcessId(Misc.processID);
        message.setId(messageId); //This message must have a new unique id 
        //Send the message to the clients of the host
        Server.messageController.queueHostChat.push(message);
        System.out.println("##-- Received forwarded message with text '" + Commands.getParseMessageText(message) + "' from Host " + message.getProcessID() + " --##");
    }

}
