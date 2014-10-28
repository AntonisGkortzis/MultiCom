package sharedresources;

import java.lang.management.ManagementFactory;

public class Misc {
	
	/**
	 * The process ID will be used to identify as process in the distributed network
	 * @return an ID of the form "12345@hostname"
	 */
	public static final String processID = ManagementFactory.getRuntimeMXBean().getName();

    
    /**
     * A unique number that is used as a message's identifier. 
     */
    public static long messageId=0;
    

    public static long getNextMessageId(){
        return ++messageId;
    }

}
