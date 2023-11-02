package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class LinkedListIndexedCollection<T> implements List<T> {
	 private static class ListNode<T>{
        T value;
        ListNode<T> next;
        ListNode<T> previous;
     }
	 
	 private static class LinkedListElementGetter<T>  implements ElementsGetter<T>{
		 
		 LinkedListIndexedCollection<T> listCollection;
		 ListNode<T> node;
		 long savedModificationCount;
		 
		 public LinkedListElementGetter(LinkedListIndexedCollection<T> list) {
			 this.listCollection = list;
			 this.node = list.first;
			 this.savedModificationCount = list.modificationCount;
		 }
		 public boolean hasNextElement() {
			 this.throwException(savedModificationCount);
			 if (node == null) return false;
			 else return true;
		 }
		 
		 public T getNextElement() {
			 this.throwException(savedModificationCount);
			 if (this.hasNextElement() == true) {
				 T result = node.value;
				 node = node.next;
				 return result;
			 }
			 else {
				 throw new NoSuchElementException();
			 }
		 }
		 private boolean throwException(long counter) {
  		   if (counter != listCollection.modificationCount) throw new ConcurrentModificationException();
  		   else {
  			   return true;
  		   }
  	   }
		public void processRemaining(Processor<T> p) {
			this.throwException(savedModificationCount);
			for(ListNode<T> el = node;el.next != null;el = el.next) {
				p.process(el.value);
			}
			
		}
		 
	 }
	 
	 // Variables 
	 
	 private int size;
	 private ListNode<T> first;
	 private ListNode<T> last;
	 private long modificationCount = 0;
	 
	 // Constructors
	 
	 public LinkedListIndexedCollection() {
		 this.size = 0;
		 this.first = null;
		 this.last = null;
	 }
	 
	public LinkedListIndexedCollection(Collection<T> other) {
		 this.addAll(other);
	 }
	 
	 // Methods
	 
	 /**
	  * Metoda dodaje predni joj objekt na kraj kolekcije,koja je primjerak razreda LinkedListIndexedCollection
	  * @param value
	  * @return NullPointerException , ako joj je predani objekt null
	  */
	 public void add(T value) {
		 
		 if (value == null) throw new NullPointerException();
		 
		 ListNode<T> newListNode = new ListNode<T>();
		 
		 newListNode.value = value;
		 newListNode.next = null;
		 newListNode.previous = last;
		 
		 if(this.last == null) {
			 first = newListNode;
		 }else {
			 last.next = newListNode;
		 }
		 last = newListNode;
		 
		 this.size+=1;
		 this.modificationCount++;
	 }
	 
	 
	 /**
	  * Metoda dohvaca element kolekcije na zadanom mjestu(index).
	  * @param index
	  * @return objekt, predstavlja element koji je dohvacen s pozicije index.
	  * @return IndexOutOfBoundsException, ako index < 0 ili index > trenutne velicine kolekcije
	  */
	 public T get(int index) {
		 
		 if (index < 0 || index > (this.size-1)) throw new IndexOutOfBoundsException();
		 
		 int middle = this.size/2;
		 
		 ListNode<T> helpNode;
		 T result;
		 
		 if (index < middle || index == middle) {
			 helpNode = first;
			 for(int i=0;i<index;i++) {
				 helpNode = helpNode.next;
			 }
			 result = helpNode.value;
		 }else {
			 helpNode = last;
			 
			 for(int i=0;i<(this.size-index-1);i++) {
				 helpNode = helpNode.previous;
			 }
			 result = helpNode.value;
		 }
		 
		 return result;
	 }
	 
	 /**
	  * Metoda brise sve elemente trenutne kolekcije.
	  */
	 public void clear() {
		 first = null;
		 last = null;
		 this.size = 0;
		 this.modificationCount++;
	 }
	 
	 /**
	  * Metoda dodaje objekt u kolekciju na zadanoj joj pozicij(position)
	  * 
	  * @param value, predstavlja objekt kojeg treba ubaciti u kolekciju
	  * @param position, predstavlja poziciju na koju treba ubaciti objekt
	  * @return NullPointerException, ako je value == null
	  * @return IndexOutOfBoudnsException, ako je position neispravan.
	  */
	 
	 public void insert(T value,int position) {
		 
		 if (value == null) throw new NullPointerException();
		 if (position < 0 || position > this.size) throw new IndexOutOfBoundsException();
		 
		 
		 ListNode<T> newListNode = new ListNode<T>();
		 newListNode.value = value;
		 if (first == null) {
			 first = newListNode;
			 last = newListNode;
		 } else {
			 ListNode<T> HelpNode = first;
			 for(int i=0;i<position;i++) {
				 HelpNode=HelpNode.next;
			 }
		 
			 if(HelpNode == null) {  // dodavanje na kraj
				 newListNode.previous = last;
				 last.next = newListNode;
				 last = newListNode;
			 } else if (HelpNode == first) { // dodavanje na pocetak
				 newListNode.next = first;
				 first.previous = newListNode;
				 first = newListNode;
			 } else {   // dodavanje u sredinu
				 newListNode.next = HelpNode;
				 newListNode.previous = HelpNode.previous;
				 HelpNode.previous.next = newListNode;
			 	 HelpNode.previous = newListNode;
			 }
		 }

		 this.size+=1; 
		 this.modificationCount++;
		 
	 }
	 
	 /**
	  * Metoda pronalazi index danog joj elementa u kolekciji, te vraca ga.
	  * @param value, objekt koji se trazi u kolekciji
	  * @return broj , index na kojem se nalazi element
	  * @return -1,  element ne postoji u kolekciji
	  */
	 
	 public int indexOf(T value) {
		 
		 if (value == null) return -1;
		 ListNode<T> helpNode = first;
		 for(int i=0;i<this.size;i++) {
			 if (helpNode.value.equals(value)) return i;
			 helpNode = helpNode.next;
		 }
		 
		 return -1;
	 }
	 
	 
	 public void remove(int index) {
	      if (index < 0 || index > this.size) throw new IndexOutOfBoundsException();
	      
	      ListNode<T> helpNode = first;
	      for(int i=0;i<index;i++) {
	    	  helpNode = helpNode.next;
	      }
	      
	      if (helpNode == first) {  // brisanje s pocetka
	    	  first = helpNode.next;
	    	  first.previous = null;
	      } else if(helpNode == last) {  // brisanje s kraja
	    	  last = helpNode.previous;
	    	  last.next = null;
	      } else if (helpNode == first && helpNode == last) { // brisanje zadnjeg elementa
	    	  this.clear();
	      }
	      else {  // brisanje elementa u sredini
	    	  
	    	  helpNode.next.previous = helpNode.previous;
	    	  helpNode.previous.next = helpNode.next;
	      }
	      this.size= this.size-1;
	      this.modificationCount++;
	 }
	 
	 
	 /**
	  * Metoda ispisuje kolekciju na ekran.
	  */
	 public void print() {
		 ListNode<T> node = first;
		 System.out.println(this.size);
		 for(int i=0;i<this.size;i++) {
			 System.out.print(node.value + " -> ");
			 node = node.next;
		 }
		 System.out.print("null");
		 System.out.println();
	 }
	 
	 /**
	  * Metoda vraca broj elemenata u kolekciji
	  * @return broj, broj elemenata u kolekciji
	  */
	 public int size() {
		 return this.size;
	 }
	 
	 
	 /**
	  * Metoda provjera nalazi li se zadani objekt u kolekciji.
	  * @param value
	  * @return true, ako se objekt nalazi u kolekciji
	  * @return false, ako se objekt ne nalazi u kolekciji
	  */
	 public boolean contains(T value) {
		 boolean contain = false;
		 ListNode<T> node=first;
		 for(int i=0;i<this.size;i++) {
			 if (node.value.equals(value)) {
				 contain = true;
			 }
		 }
		 return contain;
	 }
	 
	   /**
		 * Metoda pronalazi objekt koji je dobila u argumentima,te ako postoji brise ga.
		 *
		 * @param value, predstavlja objekt koji treba izbrisati iz kolekcije
		 * @return true, ako je objekt obrisan
		 * @return false,ako objekt nije pronadjen u kolekciji
		 */
	 
	 public boolean remove(T value) {
		 
		 boolean bool;
		 int index = this.indexOf(value);
		 
		 if (index == -1) {
			 bool = false;
		 } else {
			 bool = true;
			 this.remove(index);
		 }
		 this.modificationCount++;
		 return bool;
		 
	 }
	 
	 
	  /**
		* Metoda stvara i vraca novi niz koji puni elementima primjerka razreda LinkedListIndexedCollection.
		* Nikada ne vraca null.
		* 
		* @return Object[], predstavlja primjerak razreda Collection,ali u obliku niza.
		*/
	 public T[] toArray() {
		 
		 @SuppressWarnings("unchecked")
		T[] array = (T[]) new Object[this.size];
		 ListNode<T> node = first;
		 for (int i=0;i<this.size;i++) {
			 array[i] = node.value;
			 node= node.next;
		 }
		 
		 return array;
	 }
	 
	 /**
	  * Metoda poziva metodu process iz razreda Processor, za svaki clan primjerka razreda LinkedListIndexedCollection
	  * 
	  * @param processor, predstavlja primjerak razreda Processor iz kojeg se poziva metoda process.
	  */
	 
	 public void forEach(Processor<T> processor) {
		 ListNode<T> node = first;
		 for(int i=0;i<this.size;i++) {
			 processor.process(node.value);
			 node = node.next;
		 }
	 }	 
	 
	 public static void main(String ...args) {
		 
		
		/* Collection col1 = new LinkedListIndexedCollection();
		 Collection col2 = new LinkedListIndexedCollection();
		 col1.add("Ivo");
		 col1.add("Ana");
		 col1.add("Jasna");
		 col2.add("Jasmina");
		 col2.add("Štefanija");
		 col2.add("Karmela");
		 ElementsGetter getter1 = col1.createElementsGetter();
		 ElementsGetter getter2 = col1.createElementsGetter();
		 ElementsGetter getter3 = col2.createElementsGetter();
		 System.out.println("Jedan element: " + getter1.getNextElement());
		 System.out.println("Jedan element: " + getter1.getNextElement());
		 System.out.println("Jedan element: " + getter2.getNextElement());
		 System.out.println("Jedan element: " + getter3.getNextElement());
		 System.out.println("Jedan element: " + getter3.getNextElement());*/
		 
		 /*Collection col = new LinkedListIndexedCollection();
		 col.add("Ivo");
		 col.add("Ana");
		 col.add("Jasna");
		 ElementsGetter getter = col.createElementsGetter();
		 System.out.println("Jedan element: " + getter.getNextElement());
		 System.out.println("Jedan element: " + getter.getNextElement());
		 col.clear();
		 System.out.println("Jedan element: " + getter.getNextElement());*/

		/*class EvenIntegerTester implements Tester {
			 public boolean test(Object obj) {
			 if(!(obj instanceof Integer)) return false;
			 Integer i = (Integer)obj;
			 return i % 2 == 0;
			 }
			}
			Tester t = new EvenIntegerTester();
			System.out.println(t.test("Ivo"));
			System.out.println(t.test(22));
			System.out.println(t.test(3));
			
			
			Collection col2 = new LinkedListIndexedCollection();
			Collection col1 = new ArrayIndexedCollection();
			col1.add(2);
			col1.add(3);
			col1.add(4);
			col1.add(5);
			col1.add(6);
			col2.add(12);
			col2.addAllSatisfying(col1, new EvenIntegerTester());
			
			Object[] array = col2.toArray();
			
			for(int i=0;i<array.length;i++) {
				System.out.println(array[i]);
			}
			//col2.forEach(System.out::println);*/

	 }

	@Override
	public LinkedListElementGetter<T> createElementsGetter() {
		// TODO Auto-generated method stub
		return new LinkedListElementGetter<T>(this);
	}
	 
	 
	 
}
