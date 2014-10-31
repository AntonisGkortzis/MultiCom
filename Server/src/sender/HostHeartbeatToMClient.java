package sender;

import multithread.sockets.Server;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;

public class HostHeartbeatToMClient implements Runnable { //TODO remove

//    boolean flag;
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
//        flag=true;
//        while(flag) {
//            try {
//                Thread.sleep(Config.hostHeartbeatDelay);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            
//            String command = Commands.constructCommand(Commands.hostHeartbeat);
//            Message message = new Message(Message.MessageType.clientCommand, command);
//            Server.messageController.queueHostChat.push(message);
//        }

    }

}
