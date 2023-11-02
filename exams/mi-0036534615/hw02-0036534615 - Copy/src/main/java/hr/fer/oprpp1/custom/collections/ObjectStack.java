package hr.fer.oprpp1.custom.collections;

public class ObjectStack<T> {
     private ArrayIndexedCollection<T> arrayCollection;
     
     public ObjectStack() {
    	 this.arrayCollection = new ArrayIndexedCollection<T>();
     }
     
     public boolean isEmpty() {
    	 return arrayCollection.isEmpty();
     }
     
     public int size() {
    	 return arrayCollection.size();
     }
     
     public void push(T value) {
    	arrayCollection.add(value);
     }
     
     @SuppressWarnings("unchecked")
	public T pop() {
    	 
    	 this.EmptyStackHandling();
    	 
    	 Object element = arrayCollection.get(this.size()-1);
    	 
    	 arrayCollection.remove(this.size()-1);
    	 
    	 return (T) element;
     }
     
     public T peek() {
    	 
    	 this.EmptyStackHandling();
    	 
    	 return arrayCollection.get(this.size()-1);
    	 
     }
     
     
     public void clear() {
    	 
    	arrayCollection.clear();
     }
     
     
     private void EmptyStackHandling() {
    	 if (this.size() == 0) throw new EmptyStackException();
     }
     
     
    
     public static void main(String ...args) {
    	 Collection<String> prva = new ArrayIndexedCollection<>();
    	 Collection<Integer> druga = new ArrayIndexedCollection<>();
    	 
    	 prva.add("Ivo");
    	 prva.add("Ivka");
    	 
    	 prva.copyTransformedIntoIfAllowed(druga, Object::hashCode, n -> { return n.intValue()%2 == 0;});
    	 
     }
     
}
