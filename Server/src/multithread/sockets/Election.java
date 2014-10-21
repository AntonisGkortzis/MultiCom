package multithread.sockets;

import sharedresources.Commands;
import sharedresources.Message;
import sharedresources.Misc;
import sharedresources.Misc.MessageType;

public class Election implements Runnable {

    private HostToMHost hostToMHost;
    public Election(HostToMHost hostToMHost) {
        this.hostToMHost = hostToMHost;
    }
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    
    public void election() {
    
  
    }
    
    public void startElection() {
        String command = Commands.constructCommand(Commands.startElection);
        Message message = new Message(MessageType.mHostVote, true, Misc.getProcessID(), null, command );
        hostToMHost.sendMessage(message);
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }

}
