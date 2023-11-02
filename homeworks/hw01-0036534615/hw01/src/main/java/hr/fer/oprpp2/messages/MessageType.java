package hr.fer.oprpp2.messages;

public enum MessageType {
	HELLO(1),
	ACK(2),
	BYE(3),
	OUTMSG(4),
	INMSG(5);
	
	private int ordinalNumber;
	
	private MessageType(int ordinalNumber) {
		this.ordinalNumber = ordinalNumber;
	}
	
	public int getOrdinalNumber() {
		
		return this.ordinalNumber;
	}
	
	public static MessageType fromNumber(int messageNumber) {
		if (messageNumber == HELLO.ordinalNumber) {
			return MessageType.HELLO;
		} else if (messageNumber == ACK.ordinalNumber) {
			return MessageType.ACK;
		} else if (messageNumber == BYE.ordinalNumber) {
			return MessageType.BYE;
		} else if (messageNumber == OUTMSG.ordinalNumber) {
			return MessageType.OUTMSG;
		} else if (messageNumber == INMSG.ordinalNumber) {
			return MessageType.INMSG;
		} else {
			throw new RuntimeException("Wrong message type.");
		}
	}
	
}
