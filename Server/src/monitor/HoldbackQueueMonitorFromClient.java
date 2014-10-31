package monitor;

import java.util.Date;

import multithread.sockets.Server;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.ConnectedClient;
import sharedresources.ConnectedClientsList;
import sharedresources.ForwardMessage;
import sharedresources.Message;
import sharedresources.Misc;

public class HoldbackQueueMonitorFromClient implements Runnable {

    private boolean flag;
    
    public HoldbackQueueMonitorFromClient() {}
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    public void stop() {
        this.flag = false;
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
            for(ConnectedClient client: ConnectedClientsList.clients) {
                if(client.holdbackQueue.peek()!= null 
                        && currentTime - client.holdbackQueue.peek().getTimeReceived() > Config.holdBackQueueDelay) 
                {
                    //The message is ready to be delivered.
                    Message message = client.holdbackQueue.poll(); //pop message
                    
                    //Prepare message for delivery queues
                    storeMessageInDeliveryQueues(message);
                }
            }
        }
    }
    
    /**
     * Store a message in two delivery queues
     * One for sending to clients of host
     * One for sending to other hosts
     * @param message
     */
    private void storeMessageInDeliveryQueues(Message message) {
        message.setProcessId(Misc.processID);
        message.setId(Misc.getNextMessageId());
        Server.messageController.queueHostChat.push(message); //to send it to the clients connected on this host
        
        String command = Commands.constructCommand(Commands.forwardMessage, message.getText());
        Message newMessage = new Message(Message.MessageType.mHostChat, message.getUsername(), command, Misc.getNextMessageId());                    
        Server.messageController.queueSend.push(newMessage); //Send to other hosts    
        
        //Put the chat message in a queue for possible re-sending to other hosts
        addToRetryQueueForHosts(newMessage);        
    }

    /**
     * Put forward message in retry queue for resending with global multicast to a 
     * certain host which did not send an acknowledgement.
     * @param message
     */
    private void addToRetryQueueForHosts(Message message) {
        //create forward message with setting hosts as the receivers (true if receiver is host)  
        ForwardMessage forwardMessage = new ForwardMessage(message, message.getId(), true);
        System.out.println("$$ HostToMHost adding forwarded message [" + forwardMessage.getMessage() + "] ##");
        Server.messageController.queueSentMessagesByHostToMHost.add(forwardMessage);
    }
}
