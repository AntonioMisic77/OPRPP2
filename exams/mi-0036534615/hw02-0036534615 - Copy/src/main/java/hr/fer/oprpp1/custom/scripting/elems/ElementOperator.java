package hr.fer.oprpp1.custom.scripting.elems;


/**
 * Element that represents an operator symbol, extends {@link Element}
 * @author mkremer
 *
 */
public class ElementOperator extends Element {

	
	/**
	 * Single property of type {@link String} 
	 */
	private String symbol;


	
	/**
	 * Constructor for class {@link ElementOperator}
	 * @param symbol of type {@link String}
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}



	/**
	 * @return property symbol as {@link String}
	 */
	@Override
	public String asText() {
		return symbol;
	}




	/**
	 * 
	 * 
	 * @return <code>true</code> if this symbol property is equal to another instances, <code>false</code> otherwise 
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementOperator) || obj == null) return false;
		
		ElementOperator otherOperator = (ElementOperator) obj;
		
		return this.asText().equals(otherOperator.asText());
	}


	/**
	 * @return property symbol as {@link String}
	 */
	@Override
	public String toString() {
		return  asText();
	}
	
	
	
	
}
