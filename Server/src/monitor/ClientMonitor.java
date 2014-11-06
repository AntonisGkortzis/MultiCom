package monitor;

import java.util.Date;
import java.util.Iterator;

import sharedresources.Config;
import sharedresources.ConnectedClient;
import sharedresources.ConnectedClientsList;

/**
 * Checks if clients are dead and then removes them
 * @author mark
 *
 */
public class ClientMonitor implements Runnable {

    private final int declareDead = Config.clientHeartbeatDelay*2; //must be bigger than clientHeartBeatDelay
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        boolean flag = true;
        while(flag) {
            try {
                Thread.sleep(2000);
            } catch(InterruptedException e) {
                e.printStackTrace();
                flag = false;
            }
            long currentTime = new Date().getTime();
            Iterator<ConnectedClient> iterator = ConnectedClientsList.clients.iterator();
            while(iterator.hasNext()) {
                ConnectedClient client = iterator.next();
                if(currentTime - client.getLastUpdate().getTime() > this.declareDead) {
                    //Client can be declared dead so remove it from the list
                    System.out.println("@@-- Removing the dead Messenger " + client.getProcessID() + " --@@");
                    iterator.remove();
                }
            }
        }

    }

}
