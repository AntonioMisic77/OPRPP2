package hr.fer.oprpp1.custom.scripting.elems;

public class ElementStringTest {
	
	public static void main(String[] args) {
		ElementString s = new ElementString("AA \\n b \\t \\r \\\"");
		
		System.out.println(s.asText());
	}

}
