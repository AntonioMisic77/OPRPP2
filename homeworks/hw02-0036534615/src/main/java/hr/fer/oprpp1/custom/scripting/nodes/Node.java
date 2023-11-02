package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;


/**
 * Base class for all nodes
 * 
 * @author Marko Kremer
 * 
 * Description:
 * 		My implementation of smartparser is not working, so I have asked my peer to lend me his code, for this homework.
 *
 */
public abstract class Node {
	
	
	/**
	 * Single property type {@link ArrayIndexedCollection}
	 */
	private ArrayIndexedCollection collection;
	
	
	/**
	 * Adds a child node to the current node
	 * 
	 * @param child of type {@link Node}
	 */
	public void addChildNode(Node child) {
		if(collection == null) {
			collection = new ArrayIndexedCollection();
		}
		
		collection.add(child);
	}
	
	
	/**
	 * 
	 * @return number of children nodes of the current node
	 */
	public int numberOfChilden() {
		if(this.collection == null) return 0;
		return collection.size();
	}
	
	
	/**
	 * 
	 * Returns node located on the given index position in the collection
	 * 
	 * @param index of primitive type int
	 * @return object type {@link Node}
	 * @throws IndexOutOfBoundsException if the given index is lesser than 0 or greater than collection size minus 1
	 */
	public Node getChild(int index) {
		if(index < 0 || index > collection.size()-1) throw new IndexOutOfBoundsException();
		return (Node) collection.get(index);
	}
	
	public abstract void accept(INodeVisitor visitor);
}
