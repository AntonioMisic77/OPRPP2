package hr.fer.oprpp1.custom.scripting.elems;


/**
 * 
 * Element that represents a double constant, extends {@link Element}
 * 
 * @author Marko Kremer
 *
 */
public class ElementConstantDouble extends Element {
	
	
	/**
	 * Single double property of the class {@link ElementConstantDouble}
	 */
	private double value;

	
	/**
	 * Constructor for class {@link ElementConstantDouble}
	 * @param value of primitive type double
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	
	/**
	 * @return a string representation of the value property
	 */
	@Override
	public String asText() {
		return String.valueOf(value).replaceAll("\"","");
	}

	
	/**
	 * @return  <code>true</code> if double value equals other double value, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementConstantDouble) || obj == null) return false;
		
		ElementConstantDouble otherDouble = (ElementConstantDouble) obj;
		
		return this.asText().equals(otherDouble.asText());
	}

	
	/**
	 * returns String representation of value property
	 */
	@Override
	public String toString() {
		return asText();
	}
	
	
	
	

}
