package hr.fer.oprpp1.custom.collections;

public interface ElementsGetter<T> {
	
	boolean hasNextElement();
	
    T getNextElement();
    
    void processRemaining(Processor<T> p);
	
	
}
