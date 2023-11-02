package hr.fer.oprpp2.messages;

public class AckMessage extends Message {

	private long userId;
	
	public AckMessage(long messageNumber,long userId) {
		super(MessageType.ACK,messageNumber);
		this.userId = userId;
	}
	
	public long getUserId() {
		return this.userId;
	}
}
