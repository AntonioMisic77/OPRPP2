package hr.fer.oprpp2.messages;

public class HelloMessage extends Message {

	private String userName;
	private long key;
	
	public HelloMessage(long messageNumber,String username,long key) {
		super(MessageType.HELLO, messageNumber);
		this.userName = username;
		this.key = key;
	}
	
	public String getUsername() {
		return this.userName;
	}
	
	public long getKey() {
		return this.key;
	}
	
}
