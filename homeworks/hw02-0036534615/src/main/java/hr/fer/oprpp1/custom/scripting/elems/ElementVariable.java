package hr.fer.oprpp1.custom.scripting.elems;



/**
 * 
 * Element that represents a variable, extends {@link Element}
 * @author Marko Kremer
 *
 */
public class ElementVariable extends Element {

	
	/**
	 * Single property type {@link String}, name of the variable
	 */
	private String name;
	
	
	/**
	 * Constructor for class {@link ElementVariable}
	 * @param name of type {@link String}
	 */
	public ElementVariable(String name) {
		this.name = name;
	}



	/**
	 * Returns name property type {@link String}
	 */
	@Override
	public String asText() {
		return name.replaceAll("\"","");
	}



	/**
	 * Checks if this instance of {@link ElementVariable} is equal to another
	 * 
	 * @return <code>true</code> if this name property is equal to another's, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementVariable) || obj == null) return false;
		
		ElementVariable otherVariable = (ElementVariable) obj;
		
		return otherVariable.asText().equals(this.asText());
	}


	
	
	/**
	 * Returns name property type {@link String}
	 */
	@Override
	public String toString() {
		return asText();
	}
	
	
	
}
