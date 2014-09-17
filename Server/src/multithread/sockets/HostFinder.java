package multithread.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

class HostFinder extends Thread {
	
	public static final int PORT_AMOUNT = 4;
	public static final int START_PORT = 4444;
	
	private static List<Integer> hosts = new ArrayList<Integer>();
	
    public HostFinder() {}
    
    public void run() {
    	while(true) {
		    
    		findHost();
    		
            try {
            	sleep(5000);
		    } catch (InterruptedException e) {
		    	e.printStackTrace();
		    }
		}
    }
    
    private void findHost() {
    	int port;
    	boolean existed;
    	
    	for(int i=START_PORT; i<START_PORT+PORT_AMOUNT;i++) {
    		existed = false;
    		ServerSocket socket = null;
    	    try {
	            socket = new ServerSocket(i);
            } catch (IOException e) {
            	existed = true;
            	System.out.println("Port: " + i + " in use already");
	            if(!hosts.contains(i)) {
	            	hosts.add(i);
	            } 
            }
    	    
    	    if(!existed) {
    	    	if(socket!=null) {
	                try {
	                    socket.close();
                    } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
                    }
    	    	}
    	    	if(hosts.contains(i)) {
    	    		hosts.remove((Object) i);
    	    	}
    	    } 

    	}
    }
}