package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;


/**
 * Node that represents a tag that starts with "="
 * @author Marko Kremer
 *
 */
public class EchoNode extends Node {
	
	
	/**
	 * Single property {@link Element} array contains every element in the echo tag
	 */
	private Element[] elements;
	
	

	/**
	 * Constructor for {@link EchoNode}
	 * @param elements of type {@link Element} array
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}



	/**
	 * 
	 * @return property elements
	 */
	public Element[] getElements() {
		return elements;
	}



	/**
	 * Checks if this instance of {@link EchoNode} is equal to another
	 * 
	 * @return <code>true</code> if two instances of {@link EchoNode} have equal elements, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof EchoNode) || obj == null) return false;
		
		EchoNode otherEcho = (EchoNode) obj;
		
		if(this.elements.length != otherEcho.elements.length) return false;
		
		for(int i = 0; i < elements.length; i++) {
			if(!elements[i].equals(otherEcho.elements[i])) return false;
		}
		
		return true;
	}


	/**
	 * @return string representation of an echo tag with all its elements
	 */
	@Override
	public String toString() {
		
		String s = "{$= ";
		for(int i = 0; i < elements.length; i++) {
			s += elements[i].toString() + " ";
		}
		
		s+= "$}";
		
		return s;
	}



	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}

	
	
	
	
	

}
