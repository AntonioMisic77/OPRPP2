package hr.fer.oprpp1.custom.scripting.nodes;


/**
 * Node that has only text, extends {@link Node}
 * 
 * @author Marko Kremer
 *
 */
public class TextNode extends Node {

	
	/**
	 * Single property of type {@link String}
	 */
	private String text;
	
	

	/**
	 * Constructor for class {@link TextNode}
	 * @param text of type {@link String}
	 * 
	 */
	public TextNode(String text) {
		this.text = text;
	}



	/**
	 * 
	 * @return text representation of text property but removing escape character "\"
	 */
	public String getText() {
		String newText = text;
		if(text.contains("\\{")) {
			newText =  text.replaceAll("\\\\\\{", "\\{");
		}
		
		if(newText.contains("\\\\")) {
			newText =  newText.replaceAll("\\\\\\\\", "\\\\");
		}
		
		return newText;
	}



	/**
	 * Checks if this instance of {@link TextNode} is equal to another
	 * 
	 * @return <code>true</code> if this instances's text property is equal to another's, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TextNode) || obj == null) return false;
		
		TextNode otherTextNode = (TextNode) obj;
		
		return this.toString().trim().equals(otherTextNode.toString().trim());
	}



	@Override
	public String toString() {
		return text;
	}



	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
	
	

	
	
	
}
