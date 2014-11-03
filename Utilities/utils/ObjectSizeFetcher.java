package utils;

import java.lang.instrument.Instrumentation;

import sharedresources.Message;

public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Message message) {
    	System.out.println("=== Object size : " + instrumentation.getObjectSize(message));
//        return instrumentation.getObjectSize(message);
    	return 0;
    }
}
