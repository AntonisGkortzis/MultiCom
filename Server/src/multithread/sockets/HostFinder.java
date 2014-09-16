package multithread.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

class HostFinder extends Thread {
	
	private static final int PORT_AMOUNT = 10;
	private static final int START_PORT = 4444;
	
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
    	
    	for(int i=0; i<PORT_AMOUNT;i++) {
    		existed = false;
    		port = START_PORT+i;
    		ServerSocket socket = null;
    	    try {
	            socket = new ServerSocket(port);
            } catch (IOException e) {
            	existed = true;
            	System.out.println("Port: " + port + " in use already");
	            if(!hosts.contains(port)) {
	            	hosts.add(port);
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
    	    	if(hosts.contains(port)) {
    	    		hosts.remove(port);
    	    	}
    	    } 

    	}
    }
}