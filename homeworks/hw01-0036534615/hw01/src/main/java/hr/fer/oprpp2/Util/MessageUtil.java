package hr.fer.oprpp2.Util;

import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import hr.fer.oprpp2.messages.AckMessage;
import hr.fer.oprpp2.messages.ByeMessage;
import hr.fer.oprpp2.messages.HelloMessage;
import hr.fer.oprpp2.messages.InMessage;
import hr.fer.oprpp2.messages.Message;
import hr.fer.oprpp2.messages.MessageDeserializer;
import hr.fer.oprpp2.messages.MessageSerializer;
import hr.fer.oprpp2.messages.MessageType;
import hr.fer.oprpp2.messages.OutMessage;

public class MessageUtil {

	private static MessageDeserializer HelloDeserializer = (dis) -> new HelloMessage(dis.readLong(),dis.readUTF(),dis.readLong());
	private static MessageDeserializer AckDeserializer = (dis) -> new AckMessage(dis.readLong(),dis.readLong());
	private static MessageDeserializer ByeDeserializer = (dis) -> new ByeMessage(dis.readLong(),dis.readLong());
	private static MessageDeserializer OutDeserializer = (dis) -> new OutMessage(dis.readLong(),dis.readLong(),dis.readUTF());
	private static MessageDeserializer InDeserializer = (dis) -> new InMessage(dis.readLong(),dis.readUTF(),dis.readUTF());
	
	private static MessageSerializer<HelloMessage>  HelloSerializer = (message,dos,bos) -> {
		try {
			dos.writeByte(message.getMessageType().getOrdinalNumber());
			dos.writeLong(message.getMessageNumber());
			dos.writeUTF(message.getUsername());
			dos.writeLong(message.getKey());
			dos.close();
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error has occured.");
	};
	
	private static MessageSerializer<AckMessage> AckSeriazlier = (message,dos,bos) -> {
		try {
			dos.writeByte(message.getMessageType().getOrdinalNumber());
			dos.writeLong(message.getMessageNumber());
			dos.writeLong(message.getUserId());
			dos.close();
			return bos.toByteArray();
		}catch(Exception e) {
			
		}
		throw new RuntimeException("Error has occured.");
	};
	
	private static MessageSerializer<ByeMessage> ByeSeriazlier = (message,dos,bos) -> {
		try {
			dos.writeByte(message.getMessageType().getOrdinalNumber());
			dos.writeLong(message.getMessageNumber());
			dos.writeLong(message.getUserId());
			dos.close();
			return bos.toByteArray();
		}catch(Exception e) {
			
		}
		throw new RuntimeException("Error has occured.");
	};
	
	private static MessageSerializer<OutMessage> OutSeriazlier = (message,dos,bos) -> {
		try {
			dos.writeByte(message.getMessageType().getOrdinalNumber());
			dos.writeLong(message.getMessageNumber());
			dos.writeLong(message.getUserId());
			dos.writeUTF(message.getMessage());
			dos.close();
			return bos.toByteArray();
		}catch(Exception e) {
			
		}
		throw new RuntimeException("Error has occured.");
	};
	
	private static MessageSerializer<InMessage> InSeriazlier = (message,dos,bos) -> {
		try {
			dos.writeByte(message.getMessageType().getOrdinalNumber());
			dos.writeLong(message.getMessageNumber());
			dos.writeUTF(message.getUserName());
			dos.writeUTF(message.getMessage());
			dos.close();
			return bos.toByteArray();
		}catch(Exception e) {
			
		}
		throw new RuntimeException("Error has occured.");
	};
	
	
	
	public static Message deserializeMessage(byte[] buffer,int offset,int length) {
		try {
			ByteArrayInputStream bos = new ByteArrayInputStream(buffer, offset, length);
			DataInputStream dis = new DataInputStream(bos);
			MessageType recivedMessageType = MessageType.fromNumber(dis.readByte());
			switch(recivedMessageType) {
				case HELLO :
					return HelloDeserializer.deserialize(dis);
				case ACK:
					return AckDeserializer.deserialize(dis);
				case BYE:
					return ByeDeserializer.deserialize(dis);
				case OUTMSG:
					return OutDeserializer.deserialize(dis);
				case INMSG:
					return InDeserializer.deserialize(dis);
				default : 
					throw new RuntimeException("Wrong message type.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] serializeMessage(Message sendingMessage) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		switch(sendingMessage.getMessageType()) {
			case HELLO:
				return HelloSerializer.serialize((HelloMessage)sendingMessage, dos, bos);
			case ACK:
				return AckSeriazlier.serialize((AckMessage)sendingMessage, dos, bos);
			case BYE:
				return ByeSeriazlier.serialize((ByeMessage)sendingMessage, dos, bos);
			case OUTMSG:
				return OutSeriazlier.serialize((OutMessage)sendingMessage, dos, bos);
			case INMSG:
				return InSeriazlier.serialize((InMessage)sendingMessage, dos, bos);
			default :
				throw new RuntimeException("Wrong message type.");
		}
	}
	
}
