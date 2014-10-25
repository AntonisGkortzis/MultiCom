package sharedresources;

import java.lang.management.ManagementFactory;

public class Misc {
	
	/**
	 * The process ID will be used to identify as process in the distributed network
	 * @return an ID of the form "12345@hostname"
	 */
	public static final String processID = ManagementFactory.getRuntimeMXBean().getName();



}
