package sharedresources;

import java.util.ArrayList;
import java.util.List;

public class MessageQueue{
	private List<Message> queue = new ArrayList<>();
	
	public MessageQueue(){}
	
	/*
	 * Adds a new message in the queue sorted by it's timestamp.
	 */
	public boolean push(Message newMessage){
		boolean flag = false;
		for(int i=0; i<size(); i++){
			if(newMessage.getTimestamp() <= queue.get(i).getTimestamp()){
				queue.add(i, newMessage);
			}
		}
		
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
	public void remove(String user, int id){
		if(!isEmpty()){
			for(int i=0; i<size(); i++){
				if(this.queue.get(i).getUser().equals(user) 
						&& this.queue.get(i).getId() == id){
					this.queue.remove(i);
				}
			}
		}
	}
	
	public int size(){
		return this.size();
	}
	
	public boolean isEmpty(){
		return this.queue.isEmpty();
	}
	
	
}
