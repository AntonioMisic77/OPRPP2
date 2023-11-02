package hr.fer.oprpp2.messages;

public class InMessage extends Message{

	private String userName;
	private String message;
	
	public InMessage(long messageNumber,String userName,String message) {
		super(MessageType.INMSG,messageNumber);
		this.userName = userName;
		this.message = message;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getMessage() {
		return this.message;
	}
}
