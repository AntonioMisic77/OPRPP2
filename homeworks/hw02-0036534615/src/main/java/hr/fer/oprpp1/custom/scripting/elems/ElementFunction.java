package hr.fer.oprpp1.custom.scripting.elems;

/**
 * 
 * Element that represent a function, extends {@link Element}
 * @author Marko Kremer
 *
 */
public class ElementFunction extends Element {
	
	/**
	 * Single {@link String} property of class {@link ElementFunction} 
	 */
	private String name;
	
	

	/**
	 * Constructor for {@link ElementFunction}
	 * @param name of type {@link String}
	 */
	public ElementFunction(String name) {
		this.name = name;
	}



	/**
	 * @return property name as {@link String}
	 */
	@Override
	public String asText() {
		return name;
	}



	/**
	 * Checks if this object if equal to another
	 * @return <code>true</code> if this name property is equal to another {@link ElementFunction} name property, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementFunction) || obj == null) return false;
		
		ElementFunction otherFunction = (ElementFunction) obj;
		
		return this.asText().equals(otherFunction.asText());
	}


	
	/**
	 * @return function sign "@" + value of property name 
	 */
	@Override
	public String toString() {
		return "@" + this.asText();
	}
	
	

}
