package sharedresources;

/**
 * This class is used for creating and using one MesssageQueue() object throughout the program.
 * 
 */
public class MessageController {
	private MessageQueue queue = new MessageQueue();
	
	public MessageController(){
		System.out.println("Creating message controller..");
	}
	
	public MessageQueue getQueue(){
		return queue;
	}
	
	public void push(Message message){
		queue.push(message);
	}
	
	public Message pop(){
		return queue.pop();
	}
}
