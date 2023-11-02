package hr.fer.oprpp1.custom.collections;

import java.util.function.Function;
import java.util.function.Predicate;


public interface Collection<T> {
    
	// Methods 
	
	/**
	 *  Metoda provjerava da li je kolekcija prazna ili nije.
	 * 
	 * @return true,ako je kolekcija prazna.
	 * @return false,ako kolekcija nije prazna.
	 */
	
	 default boolean isEmpty() {
		
	    if (this.size() != 0) {
	    	return true;
	    }else {
	    	return false;
	    }
	}
	
	
	/**
	 * Metoda dohvaca broj elemenata u kolekciji.
	 * 
	 * @return broj, koji oznacava broj elemenata u kolekciji.
	 *         broj nikada ne moze biti negativan.
	 */
	
	int size();
	
	/**
	 * Metoda dodaje element u kolekciju.
	 * 
	 * @param value, oznacava objekt koji se treba dodati u kolekciju
	 */
	
	void add(T value);
	
	/**
	 * Metoda provjerava nalazi li se objekt u kolekciji.
	 * Argument moze biti bilo koji objekt ili null.
	 * 
	 * @param value,predstavlja objekt koji se trazi u kolekciji
	 * @return true,ako se objekt nalazi u kolekciji
	 * @return false,ako se objekt ne nalazi u kolekciji
	 */
	
	boolean contains(T value);
	
	/**
	 * Metoda pronalazi objekt koji je dobila u argumentima,te ako postoji brise ga.
	 *
	 * @param value, predstavlja objekt koji treba izbrisati iz kolekcije
	 * @return true, ako je objekt obrisan
	 * @return false,ako objekt nije pronadjen u kolekciji
	 */
	
	boolean remove(T value);
	
	/**
	 * Metoda stvara i vraca novi niz koji puni elementima primjerka razreda Collection.
	 * Nikada ne vraca null.
	 * 
	 * @return Object[], predstavlja primjerak razreda Collection,ali u obliku niza.
	 */
	
	T[] toArray();
	
	/**
	 * Metoda poziva metodu process iz razreda Processor, za svaki clan primjerka razreda Collection
	 * 
	 * @param processor, predstavlja primjerak razreda Processor iz kojeg se poziva metoda process.
	 */
	
	default public void forEach(Processor<T> processor) {
		ElementsGetter<T> elGetter = this.createElementsGetter();
		for(int i=0;i<this.size();i++) {
			processor.process((T) elGetter.getNextElement());
		}
	}
	
	/**
	 * Metoda dodaje elemente zadanog primjerka razreda Collection u trenutni primjerak razreda Collection.
	 * 
	 * @param other,primjerak razreda Collection iz kojeg se dodaju elementi u trenutni razred Collection.
	 */
	
	default public void addAll(Collection<T> other) {
		
		class AddProcessor implements Processor<T> {
			
			 private Collection<T> _collection;
			
			 public AddProcessor(Collection<T> collection) {
				 this._collection = collection;
			 }
			 
			 @Override
			 public void process(T value) {
				 _collection.add(value);
			 }
		}
		
		AddProcessor addProcessor = new AddProcessor(this);
		
		other.forEach(addProcessor);
		
	}
	
	/**
	 * Metoda brise sve elemente iz trenutnog primjerka razreda Collection.
	 */
	
	void clear();
	
	
	 ElementsGetter<T> createElementsGetter();
	
	default public void addAllSatisfying(Collection<T> col, Tester<T> tester) {
		ElementsGetter<T> elGetter = col.createElementsGetter();
		while(elGetter.hasNextElement()) {
			T obj = (T) elGetter.getNextElement();
			if (tester.test(obj) == true) {
				this.add(obj);
			}
		}
		
	}
	
	default public <R> void copyTransformedIntoIfAllowed(Collection<? super R> other,Function<T,R> change,Predicate<? super R> tester) {
		ElementsGetter<T> getter = this.createElementsGetter();
		while (getter.hasNextElement()) {
			R value = change.apply(getter.getNextElement());
			if (tester.test(value)) {
				other.add(value);
			}
		}
	}
	
	
	
	
	
	
	
	
}
