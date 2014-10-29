package sharedresources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used for building a queue for the messages send over the network.
 * 
 */
public class MessageQueue{
	private List<Message> queue = new ArrayList<>();
	
	public MessageQueue(){}
	
	/*
	 * Adds a new message in the queue sorted by its timestamp.
	 */
	public boolean push(Message newMessage){
//		System.out.println("## Message pushed: " + newMessage.getText());
		boolean flag = false;
/*		int size = size();
		if(!isEmpty()){
			for(int i=0; i<size; i++){
				if(newMessage.getTimestamp() <= queue.get(i).getTimestamp()){
					queue.add(i, newMessage);
					flag = true;
				}
			}
		}
		if(!flag){*/
			flag = this.queue.add(newMessage);
//			flag = true;
//		}
		return flag;
	}
	
	/*
	 * Removes and returns the first object from the queue.
	 */
	public Message pop(){
		if(!isEmpty())
			return this.queue.remove(0);
		else
			return null;
	}
	
	/*
	 * Removes and returns an object of a given index from the queue .
	 */
	public void remove(String processId, long id){
//		if(!isEmpty()){
	    Iterator<Message> iterator = this.queue.iterator();
	    while(iterator.hasNext()) {
	        Message message = iterator.next();
	        System.out.println("processid " + processId + " id " + id + " messagePId " + message.getProcessID() + " messageID " + message.getId());
	        if(message.getProcessID().equals(processId) && message.getId()==id) {
	            System.out.println("@ MessageQueue, remove a message");
	            iterator.remove();
	        }
	    }
//			for(int i=0; i<size(); i++){
//				if(this.queue.get(i).getUsername().equals(user) 
//						&& this.queue.get(i).getId() == id){
//					this.queue.remove(i);
//				}
//			}
//		}
	}
	
	public int size(){
		return this.queue.size();
	}
	
	public boolean isEmpty(){
		return this.queue.isEmpty();
	}

	public Message get(int i) {
		return this.queue.get(i);
	}

	public void clear() {
		this.queue.clear();
		
	}

}
