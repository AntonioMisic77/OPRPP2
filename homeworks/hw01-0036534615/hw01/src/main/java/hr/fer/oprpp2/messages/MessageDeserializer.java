package hr.fer.oprpp2.messages;

import java.io.DataInputStream;
import java.io.IOException;

public interface MessageDeserializer {
	public Message deserialize(DataInputStream dis) throws IOException;
}
