package hr.fer.zemris.java.custom.scripting.exec;

public interface Wrapper<T> {

	public T add(T firstArg,T secondArg);
	public T substract(T firstArg,T secondArg);
	public T multiply(T firstArg,T secondArg);
	public T divide(T firstArg,T secondArg);
	public int compare(T firstArg,T secondArg);
}
