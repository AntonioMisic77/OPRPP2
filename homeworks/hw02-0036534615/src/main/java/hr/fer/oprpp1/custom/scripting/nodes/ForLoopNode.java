package hr.fer.oprpp1.custom.scripting.nodes;



import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;


/**
 * Class that represents a for tag, extends {@link Node}
 * @author Marko Kremer
 *
 */
public class ForLoopNode extends Node {
	
	/**
	 * Property of type {@link ElementVariable}, must appear first after "FOR"
	 */
	private ElementVariable variable;
	
	/**
	 * Property of type {@link Element}, must appear after variable
	 */
	private Element startExpression;
	
	/**
	 * Property of type {@link Element}, must appear after startExpression
	 */
	private Element endExpression;
	
	/**
	 * Property of type {@link Element}, optional
	 */
	private Element stepExpression;
	
	
	
	
	/**
	 * Constructor for {@link ForLoopNode}
	 * @param variable of type {@link ElementVariable}
	 * @param startExpression of type {@link Element}
	 * @param endExpression of type {@link Element}
	 * @param stepExpression of type {@link Element}
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,Element stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}
	
	
	/**
	 * 
	 * @return variable property of type {@link ElementVariable}
	 */
	public ElementVariable getVariable() {
		return variable;
	}
	
	/**
	 * 
	 * @return startExpression of type {@link Element}
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * 
	 * @return endExpression of type {@link Element}
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * 
	 * @return stepExpression of type {@link Element}
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	/**
	 * Checks if this instance of {@link ForLoopNode} is equal to another
	 * 
	 * @return <code>true</code> if the tag elements and children nodes of forloop node are equal to another's, <code>false</code> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ForLoopNode) || obj == null) return false;
		
		ForLoopNode otherForNode = (ForLoopNode) obj;
		
		if(!otherForNode.variable.equals(this.variable) || !otherForNode.startExpression.equals(this.startExpression) || !otherForNode.endExpression.equals(this.endExpression) || !otherForNode.stepExpression.equals(this.stepExpression) || otherForNode.numberOfChilden() != this.numberOfChilden()) return false;
		
		int n = this.numberOfChilden();
		int index = 0;
		
		while(index < n) {
			if(!this.getChild(index).equals(otherForNode.getChild(index))) return false; 
			index++;
		}
		return true;
		
	}
	
	
	/**
	 * @return string representation of the {@link ForLoopNode} tag and its children
	 */
	@Override
	public String toString() {
		int n = this.numberOfChilden();
		int index = 0;
		
		String level = " ";
		
		String s = "{$ FOR " + variable.asText() + " " + startExpression.asText() + " " + endExpression.asText() + " " + stepExpression.asText() + "$}" + '\n';
		
		while(index < n) {
			s += level + this.getChild(index).toString();
			index++;
		} 
		
		s += "{$END$}"; 
		return s;
	}


	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}

	
	
	
	
}
