package hr.fer.oprpp2.messages;

public abstract class Message {

	private MessageType messageType;
	private long messageNumber;
	
	public Message(MessageType messageType, long messageNumber) {
		this.messageType = messageType;
		this.messageNumber = messageNumber;	
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public long getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(long messageNumber) {
		this.messageNumber = messageNumber;
	}
	
	
}
