package hr.fer.oprpp1.custom.collections;


/** 
 * Razred kojim se modelira struktura podataka Rijecnika.
 * @author Antonio
 *
 * @param <K>
 * @param <V>
 */
public class Dictionary<K,V> {
	
	 /**
	  * Razred kojim modeliramo Key,Value par u nasoj Mapi	
	  * @author Antonio
	  *
	  * @param <K>
	  * @param <V>
	  */
      private static class DictionaryElement<K,V>{
    	  K key;
    	  V value;
    	  
    	  public DictionaryElement(K key,V value) {
    		  this.key = key;
    		  this.value = value;
    	  }
      }
      
      
      // Variables 
      private ArrayIndexedCollection<DictionaryElement<K,V>> array;
      
      
      // Ctor
      public  Dictionary(){
    	  this.array = new ArrayIndexedCollection<DictionaryElement<K,V>>();
      }
      
      /**
       * Metoda u razredu Dictionary, koja nam vraca informaciju o popunjenosti Kolekcije
       * @return true
       * @return false
       */
      
      public boolean isEmpty() {
    	  return array.isEmpty();
      }
      
      /**
       * Metoda u razredu Dictionary, koja nam vraca broj elemenata u kolekciji.
       * @return 
       */
      public int size() {
    	  return array.size();
      }
      
      /**
       * Metoda u razredu Dictionary, koja nam brise sve elemente iz Kolekcije
       */
      public void clear() {
    	  array.clear();
      }
      
      
      /**
       * Metoda razreda Dictionary, koja sprema zadanu vrijednost pod zadanim kljucem u Kolekciju.
       * Vraca dvije moguce situacije, ako zadani kljuc vec postoji u Kolekciji. Metoda vraca null
       * Ako kljuc ipak postoji, vrati staru vrijednost koja je spremljena pod njim.
       * @param key
       * @param value
       * @return null
       * @return value
       */
      public V put(K key, V value) {
    	  if (key == null) throw new NullPointerException();
    	  DictionaryElement<K,V> oldElement = this.getDictionaryElement(key);
    	  
    	  if (oldElement == null) {
    		  array.add(new DictionaryElement<>(key,value));
    		  return null;
    	  }
    	  V result = oldElement.value;
    	  oldElement.value = value;  
    	 
    	  
    	  return result;
      }
      
      
      /**
       * Metoda Razreda Dictionary. Dohvaca vrijednost,koja se nalazi pod zadanim joj kljucem key.
       * Vraca vrijednost pod zadanim kljucem u Kolekciji ili null ako ne postoji takav zapis u kolekciji
       * @param key
       * @return value
       * @return null
       */
      public V get(Object key) {
    	  
    	  ElementsGetter<DictionaryElement<K,V>> getter = array.createElementsGetter();
    	  
    	  while(getter.hasNextElement() == true) {
    		  DictionaryElement<K,V> element = getter.getNextElement();
    		  if (element.key.equals(key)) return element.value;
    	  }
          return null;
      }
      
      
      /**
       * Metoda Razreda Dictionary. Brise zapis iz kolekcije, pod zadanim joj kljucem.
       * Vraca value od zapisa kojeg je obrisala ili null ako taj zapis ne postoji
       * @param key
       * @return null
       * @return value
       */
      
      public V remove (K key) {
    	  V value = this.get(key);
    	  if (value == null) return null;
    	  
    	  for(int i=0;i<array.size();i++) {
    		  DictionaryElement<K,V> element = array.get(i);
    		  if (element.key.equals(key)) array.remove(element);
    	  }
    	  
    	  return value;
      }
      
      /**
       * Metoda sluzi za ispisivanje kolekcije na ekran.
       */
      public void print() {
    	  
    	  ElementsGetter<DictionaryElement<K,V>> getter = array.createElementsGetter();
    	  while (getter.hasNextElement() == true) {
    		  DictionaryElement<K,V> element = getter.getNextElement();
    		  System.out.println("Key: "+ element.key + " | " + "Value: "+ element.value);
    	  }
      }
      
      /**
       * Metoda razreda Dictionary.Vraca zapis u Rijecniku pod kljucem key.
       * @param key
       * @return DictionaryElement<K,V>
       * @return null
       */
      private DictionaryElement<K,V> getDictionaryElement(K key) {
    	  ElementsGetter<DictionaryElement<K,V>> getter = array.createElementsGetter();
    	  
    	  while(getter.hasNextElement() == true) {
    		  DictionaryElement<K,V> element = getter.getNextElement();
    		  
    		  if(element.key.equals(key)) return  element;
    	  }
    	  return null;
      }
      public static void main(String[] args) {
		    Dictionary<String,Integer> dict = new Dictionary<>();
		    
		    dict.put(String.valueOf("Antonio"), Integer.valueOf(2));
		    dict.put(String.valueOf("Pero"), Integer.valueOf(5));
		    
		    dict.print();
		    
		    dict.put(String.valueOf("Antonio"), Integer.valueOf(4));
		    
		    System.out.println("----------");
		    
		    dict.print();
		    
		    
		    dict.remove(String.valueOf("Antonio"));
		    
		    System.out.println("----------");
		    
		    dict.print();
		    
		    dict.put(String.valueOf("Pero"), Integer.valueOf(456));
		    
		    System.out.println("----------");
		    
		    dict.print();
		    
	}
        
}
