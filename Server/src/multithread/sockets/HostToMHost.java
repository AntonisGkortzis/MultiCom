package multithread.sockets;

/**
 * This class is used for communication between a host and multiple hosts.
 *  - Normal hosts need to be able to send a broadcast message to detect if the master is still alive.
 *  - The master needs to send a broadcast message to all the hosts to see if one host died.
 *
 */
public class HostToMHost implements Runnable{

    public HostToMHost() {}
    
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
}
