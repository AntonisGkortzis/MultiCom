package sharedresources;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is used for building a queue for the messages send over the network.
 * 
 */
public class MessageQueue{
	private BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
	
	public MessageQueue(){}
	
	/*
	 * Adds a new message in the queue sorted by its timestamp.
	 */
	public boolean push(Message message){
		return this.queue.add(message);
	}
	
	/*
	 * Removes and returns the first object from the queue.
	 */
	public Message pop(){
	    return this.queue.poll();
	}
	
	/*
	 * Removes and returns an object of a given index from the queue .
	 */
	public void remove(String processId, long id){
	    Iterator<Message> iterator = this.queue.iterator();
	    while(iterator.hasNext()) {
	        Message message = iterator.next();
	        if(message.getProcessID().equals(processId) && message.getId()==id) {
	            iterator.remove();
	        }
	    }
	}
	
	public int size(){
		return this.queue.size();
	}
	
	public boolean isEmpty(){
		return this.queue.isEmpty();
	}

	public void clear() {
		this.queue.clear();
	}

	public Iterator<Message> iterator() {
	    return this.queue.iterator();
	}
}
