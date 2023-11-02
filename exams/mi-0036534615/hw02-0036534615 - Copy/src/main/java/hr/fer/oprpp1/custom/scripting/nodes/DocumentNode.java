package hr.fer.oprpp1.custom.scripting.nodes;


/**
 * 
 * Class that represents a document node, has no parent node, only children nodes, extends {@link Node}
 * 
 * @author Marko Kremer
 *
 */
public class DocumentNode extends Node {

	
	/**
	 * Checks if this instance of {@link DocumentNode} is equal to another
	 * @return <code>true</code> if all the childen of this document node are equal to another's, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof DocumentNode) || obj == null) return false;
		
		DocumentNode otherDocNode = (DocumentNode) obj;
		
		if(otherDocNode.numberOfChilden() != this.numberOfChilden()) return false;
		
		
		int n = this.numberOfChilden();
		int index = 0;
		while(index < n) {
			if(!otherDocNode.getChild(index).equals(this.getChild(index))) return false;
			index++;
		}
		return true;
	}

	
	/**
	 * @return String representation of all children nodes
	 */
	@Override
	public String toString() {
		int n = this.numberOfChilden();
		int index = 0;
		
		String s = "";
		
		while(index < n) {
			s +=  this.getChild(index).toString();
			index++;
		}
		
		return s;
	}


	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
	
	
}
