package hr.fer.oprpp1.custom.scripting.elems;

/**
 * 
 * Element that represents a string, extends {@link Element}
 * 
 * @author Marko Kremer
 *
 */
public class ElementString extends Element {

	/**
	 * Single {@link String} property of class {@link ElementString}
	 */
	private String value;
	
	

	/**
	 * Constructor for class ElementString
	 * @param value of type {@link String}
	 */
	public ElementString(String value) {
		this.value = value;
	}



	/**
	 * Returns property value but removing legal escape character "\"
	 */
	@Override
	public String asText() {
		
		String newValue = value;
		if(value.contains("\\n")) {
			newValue = value.replaceAll("\\\\n", "\n");
			
		}
		 
		if(newValue.contains("\\r")) {
			newValue = newValue.replaceAll("\\\\r", "\r");
		}
		if(newValue.contains("\\t")) {
			newValue =  newValue.replaceAll("\\\\t", "\t");
		}
		
		if(newValue.contains("\\\"")) {
			newValue = newValue.replaceAll("\\\\\\\"", "\"");
		}
		
		return newValue.replaceAll("\"","");
		
	}



	/**
	 * Check if this instance of elementString is equal to another
	 * @return <code>true</code> if value property is equal to another's value property, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementString) || obj == null) return false;
		
		ElementString otherString = (ElementString) obj;
		
		return otherString.toString().equals(this.toString());
	}



	/**
	 * @return value property
	 */
	@Override
	public String toString() {
		return  value;
	}
	
	
	
	
}
