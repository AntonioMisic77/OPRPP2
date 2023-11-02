package hr.fer.zemris.java.custom.scripting.exec;

public class DoubleWrapper  implements Wrapper<Double>{

	@Override
	public Double add(Double firstArg, Double secondArg) {
		return firstArg+secondArg;
	}

	@Override
	public Double substract(Double firstArg, Double secondArg) {
		return firstArg-secondArg;
	}

	@Override
	public Double multiply(Double firstArg, Double secondArg) {
		return firstArg*secondArg;
	}

	@Override
	public Double divide(Double firstArg, Double secondArg) {
		if(secondArg.equals(Double.valueOf(0.0))) {
			throw new RuntimeException("Can't divide with 0.");
		}
		return firstArg/(Double) secondArg;
	}

	@Override
	public int compare(Double firstArg, Double secondArg) {
		return firstArg.compareTo(secondArg);
	}

}
