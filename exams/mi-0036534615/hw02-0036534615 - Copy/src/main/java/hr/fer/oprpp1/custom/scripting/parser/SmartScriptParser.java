package hr.fer.oprpp1.custom.scripting.parser;

import java.util.Arrays;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementOperator;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptToken;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.Node;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;


/**
 * Class that represents a parser, uses {@link SmartScriptLexer}
 * @author Marko Kremer
 *
 */
public class SmartScriptParser {

	
	/**
	 * Property of type {@link SmartScriptLexer}
	 */
	private SmartScriptLexer lexer;
	
	/**
	 * Property of type {@link ObjectStack}
	 */
	private ObjectStack<Node> stack;
	
	/**
	 * Property of type {@link DocumentNode}
	 */
	private DocumentNode documentNode;
	
	
	
	/**
	 * 
	 * Constructor for class {@link SmartScriptParser}
	 * 
	 * @param documentBody of type {@link String}
	 */
	public SmartScriptParser(String documentBody) {
		if(documentBody == null) throw new SmartScriptParserException(); 
		lexer = new SmartScriptLexer(documentBody);
		stack = new ObjectStack<Node>();
		documentNode = new DocumentNode();
		parse();
	}

	
	/**
	 * Method that parses the document, uses {@link SmartScriptLexer} to generate tokens
	 */
	private void parse() {
		stack.push(documentNode);
		SmartScriptToken token = null; 
		while(token == null || token.getType() != SmartScriptTokenType.EOF) {
			Node onStack = (Node)(stack.peek());
			
			token = lexer.nextToken();
			
			System.out.println(token.getType().toString());
			if(token.getType() == SmartScriptTokenType.EOF) {
				//if(!((Node)(stack.peek())).equals(documentNode)) throw new SmartScriptParserException();
				break;
			}
			
			if (token.getType() == SmartScriptTokenType.TEXT) {
				TextNode textNode = new TextNode((String) token.getValue());
				
				onStack.addChildNode(textNode);
				
			} else if(token.getType() == SmartScriptTokenType.START_TAG) {
				token = lexer.nextToken();
				if(token.getType() == SmartScriptTokenType.VARIABLE) {
					if(((String)token.getValue()).toUpperCase().equals("FOR")) {
					
						ElementVariable variable = null;
						token = lexer.nextToken();
						if(token.getType() == SmartScriptTokenType.VARIABLE) {
							variable = new ElementVariable((String) token.getValue()); 
						} else {
							throw new SmartScriptParserException();
						}
						
						token = lexer.nextToken();
						Element e = null;
						if(token.getType() == SmartScriptTokenType.INTEGER) {
							e = new ElementConstantInteger(Integer.parseInt((String) token.getValue()));
						} else if(token.getType() == SmartScriptTokenType.DOUBLE) {
							e = new ElementConstantDouble(Double.parseDouble((String) token.getValue()));
						} else if(token.getType() == SmartScriptTokenType.STRING) {
							e = new ElementString((String) token.getValue());
						} else if(token.getType() == SmartScriptTokenType.VARIABLE) {
							e = new ElementVariable((String) token.getValue());
						} else {
							throw new SmartScriptParserException();
						}
						
						token = lexer.nextToken();
						Element e2 = null;
						if(token.getType() == SmartScriptTokenType.INTEGER) {
							e2 = new ElementConstantInteger(Integer.parseInt((String) token.getValue()));
						} else if(token.getType() == SmartScriptTokenType.DOUBLE) {
							e2 = new ElementConstantDouble(Double.parseDouble((String) token.getValue()));
						} else if(token.getType() == SmartScriptTokenType.STRING) {
							e2 = new ElementString((String) token.getValue());
						} else if(token.getType() == SmartScriptTokenType.VARIABLE) {
							e2 = new ElementVariable((String) token.getValue());
						} else {
							throw new SmartScriptParserException();
						}
						
						token = lexer.nextToken();
						Element e3 = null;
						if(token.getType() == SmartScriptTokenType.INTEGER) {
							e3 = new ElementConstantInteger(Integer.parseInt((String) token.getValue()));
						} else if(token.getType() == SmartScriptTokenType.DOUBLE) {
							e3 = new ElementConstantDouble(Double.parseDouble((String) token.getValue()));
						} else if(token.getType() == SmartScriptTokenType.STRING) {
							e3 = new ElementString((String) token.getValue());
						} else if(token.getType() == SmartScriptTokenType.VARIABLE) {
							e3 = new ElementVariable((String) token.getValue());
						} else if (token.getType() == SmartScriptTokenType.END_TAG){
							ForLoopNode loopNode = new ForLoopNode(variable, e, e2, e3);
							onStack.addChildNode(loopNode);
							stack.push(loopNode);
							continue;
						} else {
							throw new SmartScriptParserException();
						}
						
						token = lexer.nextToken();
						if(token.getType() != SmartScriptTokenType.END_TAG) {
							throw new SmartScriptParserException();
						} else {
							ForLoopNode loopNode = new ForLoopNode(variable, e, e2, e3);
							onStack.addChildNode(loopNode);
							stack.push(loopNode);
						}
					} else if(((String)token.getValue()).toUpperCase().equals("END")) {
						stack.pop();
						/*if(stack.isEmpty()) {
							throw new SmartScriptParserException();
						}*/
					}
					
					
				} else if(token.getType() == SmartScriptTokenType.OPERATOR) {
					
					if(((String)token.getValue()).equals("=")) {
					
						Element[] elements = new Element[0];
						token = lexer.nextToken();
						while(token.getType() != SmartScriptTokenType.END_TAG) {
							
							
							
							Element e = null;
							if(token.getType() == SmartScriptTokenType.INTEGER) {
								e = new ElementConstantInteger(Integer.parseInt((String) token.getValue()));
							} else if(token.getType() == SmartScriptTokenType.DOUBLE) {
								e = new ElementConstantDouble(Double.parseDouble((String) token.getValue()));
							} else if(token.getType() == SmartScriptTokenType.OPERATOR) {
								e = new ElementOperator((String) token.getValue());
							} else if(token.getType() == SmartScriptTokenType.FUNCTION) {
								e = new ElementFunction((String) token.getValue());
							} else if(token.getType() == SmartScriptTokenType.STRING) {
								e = new ElementString((String) token.getValue());
							} else if(token.getType() == SmartScriptTokenType.VARIABLE) {
								e = new ElementVariable((String) token.getValue());
							}
							elements = Arrays.copyOf(elements, elements.length+1);
							elements[elements.length-1] = e;
							
							token = lexer.nextToken();
							EchoNode echoNode = new EchoNode(elements);
							if(token.getType() == SmartScriptTokenType.END_TAG) {
								onStack.addChildNode(echoNode);
							} 
							
							
						}
					}
					

				} else {
					throw new SmartScriptParserException();
				}
			} 
			
		}
		
		
		
	}

	public DocumentNode getDocumentNode() {
		return this.documentNode;
	}
}
