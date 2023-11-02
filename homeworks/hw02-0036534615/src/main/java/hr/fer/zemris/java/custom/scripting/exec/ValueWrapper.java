package hr.fer.zemris.java.custom.scripting.exec;


public class ValueWrapper {


	private Object value;
	
	private int compareResult;
	
	public ValueWrapper(Object value) {
		this.checkArgumentType(value);
		this.value = value;
	}
	
	public void add(Object incValue) {
		this.checkArgumentType(incValue);
		
		this.parseAritmeticOperation(this.value, incValue, "add");
	}
	
	public void substract(Object decValue) {
		this.checkArgumentType(decValue);
		
		this.parseAritmeticOperation(this.value, decValue, "subtract");
	}
	
	public void multiply(Object mulValue) {
		this.checkArgumentType(mulValue);
		
		this.parseAritmeticOperation(this.value, mulValue, "multiply");
	}
	
	public void divide(Object divValue) {
		this.checkArgumentType(divValue);
		
		this.parseAritmeticOperation(this.value, divValue, "divide");
	}
	
	public int numCompare(Object withValue) {
		this.checkArgumentType(withValue);
		
		this.parseAritmeticOperation(this.value, withValue, "compare");
		
		return compareResult;
	}
	private void parseAritmeticOperation(Object firstArgument,Object secondArgument,String operator) {
		
		firstArgument = this.parseSingleArgument(firstArgument);
		secondArgument = this.parseSingleArgument(secondArgument);
		
		
		if(firstArgument instanceof Double || secondArgument instanceof Double) {
			
			 Wrapper<Double> wrapper = new DoubleWrapper();
			
			if(firstArgument instanceof Integer) {
				firstArgument = this.parseToDouble((Integer)firstArgument);
				
			} else if(secondArgument instanceof Integer) {
				secondArgument = this.parseToDouble((Integer)secondArgument);
			}
			
			this.executeOperation((Double)firstArgument, (Double)secondArgument, operator, wrapper);
			
		} else {
			Wrapper<Integer> wrapper = new IntegerWrapper();
			this.executeOperation((Integer)firstArgument, (Integer)secondArgument, operator, wrapper);
		}
		
		
	}
	
	private <T> void executeOperation(T arg1,T arg2,String operator,Wrapper<T> wrapper) {
		
		switch(operator) {
			case "add":
				this.value = wrapper.add(arg1, arg2);
				break;
				
			case "subtract":
				this.value = wrapper.substract(arg1, arg2);
				break;
			
			case "multiply":
				this.value = wrapper.multiply(arg1, arg2);
				break;
				
			case "divide":
				this.value = wrapper.divide(arg1, arg2);
				break;
				
			case "compare":
				this.compareResult = wrapper.compare(arg1, arg2);
				break;
			default:
				throw new RuntimeException("Unsupported aritmetic operation");		
		}
	}
	
	private Object parseSingleArgument(Object argument) {
		if(argument == null) {
			argument = Integer.valueOf(0);
		} else if(argument instanceof String) {
			if(
			   ((String) argument).contains(".") || 
			   ((String) argument).contains("E")) {
				
				argument = this.parseToDouble((String)argument);
			} else {
				argument = this.parseToInteger((String)argument);
			}
		}
		
		return argument;
	}

	private Double parseToDouble(String value) {
		try {
			return Double.parseDouble(value);
		}catch(Exception e) {
			throw new RuntimeException("Wrong double format.");
		}
		
	}
	
	private Double parseToDouble(Integer value) {
		return Double.valueOf(value.doubleValue());
	}
	
	private Integer parseToInteger(String value) {
		try {
			return Integer.parseInt(value);
		}catch(Exception e) {
			throw new RuntimeException("Wrong integer format.");
		}
		
	}
	
	private boolean checkArgumentType(Object argument) {
		boolean approved = false;
		
		if(argument == null) approved = true;
		
		if(argument instanceof Integer) approved = true;
		
		if(argument instanceof Double) approved = true;
		
		if(argument instanceof String) approved = true;
		
		if(approved) {
			return true;
		} else {
			throw new RuntimeException("Argument is not aritmetic type");
		}
   }
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.checkArgumentType(value);
		this.value = value;
	}
	
	
	public static void main(String[] args) {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());
		
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.add(v4.getValue()); 
		
		
		ValueWrapper v5 = new ValueWrapper("12");
		ValueWrapper v6 = new ValueWrapper("14");
		System.out.println(v5.numCompare(v6.getValue())); // v5 now stores Integer(13); v6 still stores Integer(1).
		
		
		
		
	}

	
}
