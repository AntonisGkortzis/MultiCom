package monitor;

import java.util.Date;

import sharedresources.Config;
import client.Client;

public class HostMonitor implements Runnable { //TODO remove this class!

    private Client client;
    private boolean flag;
    
    public HostMonitor(Client client) {
        this.client = client;
    }
//    
//    public void start() {
//        Thread t = new Thread(this);
//        t.start();
//    }
//    
//    public void stop() {
//        flag=false;
//    }
    @Override
    public void run() {
//        flag = true;
//        //for startup connections
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        while(flag) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            long currentTime = new Date().getTime();
//            if(currentTime-client.lastHostUpdate > Config.hostHeartbeatDelay*2) {
//                System.out.println("Sadly our host died.");
//                
//            }
//        }

    }

}
