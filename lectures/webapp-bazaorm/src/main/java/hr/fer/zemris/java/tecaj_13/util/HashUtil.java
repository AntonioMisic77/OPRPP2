package hr.fer.zemris.java.tecaj_13.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

	
	public static String encrypt(String text,String algoritham)  {
			MessageDigest md;
			try {
				md = MessageDigest.getInstance(algoritham);
				byte[] messageDigest = md.digest(text.getBytes());
			
				BigInteger no = new BigInteger(1,messageDigest);
			
				String hashText = no.toString(16);
			
				while(hashText.length() < 32) {
					hashText = "0" + hashText;
				}
				return hashText;
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
			
					
	}
}
