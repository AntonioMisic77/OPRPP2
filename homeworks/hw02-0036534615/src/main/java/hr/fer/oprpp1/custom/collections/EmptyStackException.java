package hr.fer.oprpp1.custom.collections;

public class EmptyStackException extends RuntimeException {
        
	public EmptyStackException(String message) {
		super(message);
	}
	
	public EmptyStackException() {
		super();
	}
}
