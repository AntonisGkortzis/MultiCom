package sharedresources;

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
}
