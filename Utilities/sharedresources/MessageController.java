package sharedresources;

public class MessageController {
	private static MessageQueue queue = new MessageQueue();
	
	public MessageController(){
		System.out.println("Creating message controller..");
	}
	
	public static MessageQueue getQueue(){
		return queue;
	}
	
	public static void push(Message message){
		queue.push(message);
	}
	
	public static Message pop(){
		return queue.pop();
	}
}
