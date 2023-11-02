package hr.fer.oprpp2.messages;

public class OutMessage extends Message {

	private long userId;
	private String message;
	
	public OutMessage(long messageNumber,long userId,String message) {
		super(MessageType.OUTMSG,messageNumber);
		this.userId = userId;
		this.message = message;
	}
	
	
	public long getUserId() {
		return this.userId;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
