package hr.fer.oprpp1.custom.collections;

public interface List<T> extends Collection<T> {

	abstract T get(int index);
	abstract void insert(T value, int position);
	abstract int indexOf(T value);
	abstract void remove(int index);
	
}
