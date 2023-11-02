package hr.fer.oprpp2.messages;

public class ByeMessage extends Message {
	
	private long userId;
	
	public ByeMessage(long messageNumber, long userId) {
		super(MessageType.BYE,messageNumber);
		this.userId = userId;
	}
	
	public long getUserId() {
		return this.userId;
	}
	
}
