package sharedresources;

import java.lang.management.ManagementFactory;

public class Misc {
	/**
	 * Enumeration to specify to type of process that needs to receive/send
	 * @author mark
	 */
	public static enum MessageType {
		hostAsReceiver, // if the message's destination is a host.
		hostAsSender,  // if the message's sender is a host.
		multipleReceivers // if the message should be delivered to a group of listeners.
	}
	
    /**
     * The process ID will be used to identify as process in the distributed network
     * @return an ID of the form "12345@hostname"
     */
    public static String getProcessID() {
		return ManagementFactory.getRuntimeMXBean().getName();
	}
}
