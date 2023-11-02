package hr.fer.oprpp2.messages;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public interface MessageSerializer<T> {

	public byte[] serialize (T sendingMessage, DataOutputStream dos,ByteArrayOutputStream bos);
}
