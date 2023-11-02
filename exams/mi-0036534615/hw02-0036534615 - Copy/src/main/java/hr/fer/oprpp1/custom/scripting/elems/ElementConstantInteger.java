package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element that represent a constant integer, extends {@link Element}
 * 
 * @author Marko Kremer
 *
 */
public class ElementConstantInteger extends Element {

	/**
	 * Single property of primitive type int
	 */
	private int value;
	
	

	/**
	 * Constructor for class {@link ElementConstantInteger}
	 * @param value of primitive type int
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}



	/**
	 * @return String representation of value property
	 */
	@Override
	public String asText() {
		return String.valueOf(value).replaceAll("\"", "");
	}



	/**
	 * 
	 * Check if this object is equal to another object
	 * 
	 * @return <code>true</code> if other objects value if equal to this one's, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementConstantInteger) || obj == null) return false;
		
		ElementConstantInteger otherInteger = (ElementConstantInteger) obj;
		
		return this.asText().equals(otherInteger.asText());
	}



	/**
	 * @return String representation of value property
	 */
	@Override
	public String toString() {
		return asText();
	}
	
	
	
	
}
