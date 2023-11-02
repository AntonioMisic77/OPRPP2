package hr.fer.zemris.java.custom.scripting.exec;

public class IntegerWrapper implements Wrapper<Integer> {

	@Override
	public Integer add(Integer firstArg, Integer secondArg) {
		return firstArg+secondArg;
	}

	@Override
	public Integer substract(Integer firstArg, Integer secondArg) {
		return firstArg-secondArg;
	}

	@Override
	public Integer multiply(Integer firstArg, Integer secondArg) {
		return firstArg*secondArg;
	}

	@Override
	public Integer divide(Integer firstArg, Integer secondArg) {
		if(secondArg.equals(Integer.valueOf(0))) {
			throw new RuntimeException("Can't divide by zero.");
		}
		
		return firstArg/secondArg;
	}

	@Override
	public int compare(Integer firstArg, Integer secondArg) {
		return firstArg.compareTo(secondArg);
	}

}
