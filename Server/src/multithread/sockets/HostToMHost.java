package multithread.sockets;

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
