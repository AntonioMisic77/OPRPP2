package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Stack;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementOperator;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.INodeVisitor;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

public class SmartScriptEngine {
	
	private DocumentNode documentNode;
	private RequestContext requestContext;
	
	private ObjectMultistack multistack = new ObjectMultistack();
	
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				SmartScriptEngine.this.requestContext.write(node.getText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			Integer start = Integer.parseInt(node.getStartExpression().asText());
			Integer end = Integer.parseInt(node.getEndExpression().asText());
			Integer step = Integer.parseInt(node.getStepExpression().asText());
			
			if(start > end) {
				throw new IllegalArgumentException();
			}
			
			String variableName = node.getVariable().asText();
			multistack.push(variableName, new ValueWrapper(start));
			
			for(int i=start;i<end;i+=step) {
				for(int index=0;index<node.numberOfChilden();index++) {
					node.getChild(index).accept(this);
				}
				
				ValueWrapper variableValue = multistack.pop(variableName);
				variableValue.add(step);
				multistack.push(variableName, variableValue);
			}
			
			multistack.pop(variableName);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> stack = new Stack<Object>();
			
		    for(Element echoElement: node.getElements()) {
		    	
		    	if(echoElement instanceof ElementConstantInteger) {
		    		stack.push(Integer.parseInt(((ElementConstantInteger)echoElement).asText()));
		    		
		    	} else if(echoElement instanceof ElementConstantDouble) {
		    		
		    		stack.push(Double.parseDouble(((ElementConstantDouble)echoElement).asText()));
		    	} else if (echoElement instanceof ElementString) {
		    		stack.push(((ElementString)echoElement).asText());
		    	} else if (echoElement instanceof ElementVariable) {
		    		
		    		String variableName = echoElement.asText();
		    		
		    		try {
		    			multistack.peek(variableName);
		    		}catch(Exception e) {
		    			stack.push(0);
		    			continue;
		    		}
		    		stack.push(multistack.peek(variableName).getValue());
		    		
		    	} else if (echoElement instanceof ElementOperator) {
		    		
		    		ValueWrapper firstArgument = new ValueWrapper(stack.pop());
		    		ValueWrapper secondArgument = new ValueWrapper(stack.pop());
		    		
		    		executeOperation(firstArgument,secondArgument,((ElementOperator)echoElement).asText());
		    		
		    		stack.push(firstArgument.getValue());
		    		
		    	} else if(echoElement instanceof ElementFunction) {
		    		
		    		String functionName = ((ElementFunction)echoElement).asText();
		    		
		    		executeFunction(functionName,stack);
		    	}
		    	
		    }
		    
		    if(!stack.isEmpty()) {
	    		
	    		for(Object stackEl : stack.subList(0, stack.size())) {
	    			try {
						requestContext.write(String.valueOf(stackEl));
					} catch (IOException e) {
					}
	    		}
	    	}
	
		}


		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i=0;i<node.numberOfChilden();i++) {
				node.getChild(i).accept(visitor);
			}
		    try {
				SmartScriptEngine.this.requestContext.write("\r\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void executeFunction(String funName,Stack<Object> stack) {
			
			switch(funName) {
				case "sin":
					executeSin(stack);
					break;
				case "decfmt":
					executeDecfmt(stack);
					break;
				case "dup":
					executeDuplication(stack);
					break;
				case "swap":
					executeSwap(stack);
					break;
				case "setMimeType":
					executeSetMimeType(stack);
					break;
				case "paramGet":
					executeGetParam(stack);
					break;
				case "pparamGet":
					executeGetPersistenParam(stack);
					break;
				case "pparamSet":
					executeSetPersistentParam(stack);
					break;
				case "tparamGet":
					executeGetTemporaryParam(stack);
					break;
				case "tparamSet":
					executeSetTemporaryParam(stack);
					break;
				case "tparamDel":
					executeDeleteTemporaryParam(stack);
					break;
				default:
					throw new RuntimeException("Wrong function type");
			}
			
		}
		
		
		private void executeSin(Stack<Object> stack) {
			Object argument = stack.pop();
			
			Double value = 0.0;
			
			if(argument instanceof Integer) {
				value = ((Integer) argument).doubleValue();
			}else if(argument instanceof Double) {
				value = (Double) argument;
			}else {
			
			}
			
			stack.push(Math.sin((value * Math.PI) / (double) 180));
		}
		
		private void executeDecfmt(Stack<Object> stack) {
			DecimalFormat decimalFormater = new DecimalFormat((String) stack.pop());
			stack.push(decimalFormater.format(stack.pop()));
		}
		
		private void executeDuplication(Stack<Object> stack) {
			stack.push(stack.peek());
		}
		
		private void executeSwap(Stack<Object> stack) {
			Object first = stack.pop();
			Object second = stack.pop();
			
			stack.push(first);
			stack.push(second);
		}
			
		private void executeSetMimeType(Stack<Object> stack) {
		   SmartScriptEngine.this.requestContext.setMimeType(String.valueOf(stack.pop()));
		}
		
		private void executeGetParam(Stack<Object> stack) {
			Object defValue = stack.pop();
			Object name = stack.pop();
			System.out.println("tu sam");
			System.out.println((String)name.toString());
			String value = SmartScriptEngine.this.requestContext.getParameters(String.valueOf(name).replaceAll("\"", ""));
			System.out.println(value);
			stack.push(value == null ? defValue : value);
		}
		
		private void executeGetPersistenParam(Stack<Object> stack) {
			Object defValue = stack.pop();
			Object name = stack.pop();
			
			String value = SmartScriptEngine.this.requestContext.getPersistentParameters(String.valueOf(name).replaceAll("\"", ""));
			System.out.println(value);
			stack.push(value == null ? defValue : value);
		}
		
		private void executeSetPersistentParam(Stack<Object> stack) {
			Object name = stack.pop();
			Object value = stack.pop();
			
			SmartScriptEngine.this.requestContext.setPersistentParameter(String.valueOf(name).replaceAll("\"", ""),String.valueOf(value).replaceAll("\"", ""));
		}
		
		private void executeGetTemporaryParam(Stack<Object> stack) {
			Object defValue = stack.pop();
			Object name = stack.pop();
			
			String value = SmartScriptEngine.this.requestContext.getTemporaryParameter(String.valueOf(name).replaceAll("\"", ""));
			
			stack.push(value == null ? defValue : value);
		}
		
		private void executeSetTemporaryParam(Stack<Object> stack) {
			Object name = stack.pop();
			Object value = stack.pop();
			
			SmartScriptEngine.this.requestContext.setTemporaryParameter(String.valueOf(name), String.valueOf(value));
			
		}
		
		private void executeDeleteTemporaryParam(Stack<Object> stack) {
			Object name = stack.pop();
			
			SmartScriptEngine.this.requestContext.removePersistentParameter(String.valueOf(name));
		}
		
		private void executeOperation(ValueWrapper firstArg,ValueWrapper secondArg,String operator) {
			
			switch(operator) {
				case "+":
					firstArg.add(secondArg.getValue());
					break;
				case "-":
					firstArg.substract(secondArg.getValue());
					break;
				case "*":
					firstArg.multiply(secondArg.getValue());
					break;
				case "/":
					firstArg.divide(secondArg.getValue());
					break;
				default:
					throw new RuntimeException("Invalid parsed operator.");
			}
		}
		
	};
	
	public SmartScriptEngine(DocumentNode documentNode,RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}
	
	public void execute() {
		documentNode.accept(visitor);
	}
	
	public static void main(String[] args) {
		  
	}
}
