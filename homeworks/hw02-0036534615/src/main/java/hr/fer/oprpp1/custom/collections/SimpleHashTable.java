package hr.fer.oprpp1.custom.collections;

import java.lang.Math;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleHashTable<K,V> implements Iterable<SimpleHashTable.TableEntry<K, V>> {
	
      static class TableEntry<K,V>{
    	   K key;
    	   V value;
    	   TableEntry<K,V> next;
    	   
    	   public TableEntry() {
    		   this.next = null;
    	   }
    	   
    	   public TableEntry(K key,V value) {
    		   this.key = key;
    		   this.value = value;
    		   this.next = null;
    	   }

    	   public V getValue() {
			return value;
    	   }

    	   public void setValue(V value) {
			this.value = value;
    	   }

    	   public K getKey() {
			return key;
    	   }
    	   
      }
      
      private class IteratorImple  implements Iterator<SimpleHashTable.TableEntry<K, V>>{

    	private TableEntry<K,V> Entry = null;
    	private TableEntry<K,V> LastEntry = null;
    	private int expectedCounter;
    	private int slot = 0;
    	private int returns = 0;
    	private boolean changeSlot = true;
    	
    	public IteratorImple() {
    		this.expectedCounter = SimpleHashTable.this.modificationCount;
    	}
    	
		@Override
		public boolean hasNext() {
			if (expectedCounter != SimpleHashTable.this.modificationCount) {
				throw new ConcurrentModificationException();
			}
			if (this.returns == SimpleHashTable.this.size) {
				  return false;  
			}
			return true;
		}

		@Override
		public TableEntry<K, V> next() {
			if (this.hasNext() == true) {
				if (changeSlot == true) {
					
					changeSlot = false;
					Entry = SimpleHashTable.this.table[slot];
					
					while(Entry.next == null) {
						slot++;
						if (slot == SimpleHashTable.this.table.length) break;
						Entry = SimpleHashTable.this.table[slot];
						
					}
					Entry = Entry.next;
				}
			    
				LastEntry = Entry;
				
				if (Entry.next == null) {
					slot++;
					changeSlot = true;
				
				}else {
					Entry = Entry.next;	
				}
				
			    
				this.returns++;
				return LastEntry;
				
			}else {
				throw new NoSuchElementException();
			}
			
		}
		
		public void remove() {
			/*System.out.println("Key: "+LastEntry.key + " Value: "+LastEntry.value);
			System.out.println("---------------------------------------");
		    System.out.println("Key: "+Entry.key + " Value: "+Entry.value);
			*/
			
			if (expectedCounter != SimpleHashTable.this.modificationCount) {
				throw new ConcurrentModificationException();
			}
			
			
			if (SimpleHashTable.this.containsKey(LastEntry.key) == true) {
				this.expectedCounter++;
			    SimpleHashTable.this.remove(LastEntry.key);
			    this.returns--;
			}else {
				throw new IllegalStateException();
			}
			
		}
		
    	  
      }
      // Variables
      private TableEntry<K,V>[] table;
      private int size;
      private int modificationCount = 0; 
       
      @SuppressWarnings("unchecked")
	  public SimpleHashTable() {
    	  this.table = (TableEntry<K,V>[]) new TableEntry[16];
    	  this.size = 0;
    	  
    	  this.tableInitialization();
      }
      
      @SuppressWarnings("unchecked")
	  public SimpleHashTable(int initialCapacity) {
    	 if (initialCapacity < 1 ) throw new IllegalArgumentException();
    	 
    	 Double logOf2 = (Math.log10(initialCapacity)/Math.log10(2));
    	 Double roundedLog = Math.ceil(logOf2);
    	 int potCapacity = roundedLog.intValue();
    	 
    	 int capacity=1;
    	 for(int i=0;i<potCapacity;i++) {
    		 capacity*=2;
    	 }
    	 
    	 this.table = (TableEntry<K,V>[]) new TableEntry[capacity];
    	 
    	 this.tableInitialization();
    	 
       }
      
      public V put(K key, V value) {
    	    if (key == null) throw new NullPointerException();
    	    
    	    Double calculation = (this.size / (double) this.table.length);
    	    
    	    if (calculation >= 0.75) {
    	    	this.RerangeTable();
    	    }
    	        
    	    return this.add(key, value);
       }
       
      @SuppressWarnings("unchecked")
	  public V get(Object key) {
    	   
    	  int index = this.calculateSlot((K)key);
    	  
    	  
         TableEntry<K,V> element  = table[index].next;
    		   
    	  while(element != null) {
    		  
    		    if (element.key.equals(key)) return element.value;
    			 element = element.next;
    	   }
    		   
    	   
    	   return null;
       }
       
      public int size() {
    	   return this.size;
       }
      
      @SuppressWarnings("unchecked")
      public boolean containsKey(Object key) {
    	  
    	  if (key == null) throw new NullPointerException();
    	   
    	  int index = this.calculateSlot((K)key);
    	  TableEntry<K,V> element  = table[index].next;
    	  
    	  while(element != null) {
    		  
  		    if (element.key.equals(key)) return true;
  			element = element.next;
    	  }
  		   
  	   
  	      return false;  
      }
      
      public boolean containsValue(Object value) {
    	  
    	  for(int i=0;i<table.length;i++) {
    		  TableEntry<K,V> element  = table[i].next;
    		  
    		  while(element != null) {
        		  
    	  		    if (element.value.equals(value)) return true;
    	  			element = element.next;
    	      }
    	  		   
    	  }
    	  
    	  return false;
      }   
      
      @SuppressWarnings("unchecked")
      public V remove(Object key) {
    	
		int slot = this.calculateSlot((K)key);
		V oldValue = null;
		
    	TableEntry<K,V> element = table[slot].next;
    	TableEntry<K,V> previous = element;
    	
    	while(element != null) {
    		
    		if (element.key.equals(key)) {
    			oldValue = element.value;
    			previous.next = element.next;
    			element.next = null;
    			break;
    		}
    		previous = element;
    		element = element.next;
    	}
    	this.size--;
    	this.modificationCount++;
    	return oldValue;
      }
      
      public boolean isEmpty() {
    	  
    	  if (this.size > 0) return false;
    	  else return true;
      }
      
      public String toString() {

    	  String result="[";
    	  
    	  for(int i=0;i<table.length;i++) {
    		  TableEntry<K,V> element = table[i].next;
    		  
    		  while( element != null) {
    			  
    			  result+=element.key+"="+element.value;
    			  result+=", ";
    			  
    			  element = element.next;
    		  }
    	  }
    	  String finalResult = result.substring(0,result.length()-2);
    	  finalResult+="]";
    	  return finalResult;
      }
      
      public void clear() {
    	  for(int i=0;i<table.length;i++) {
    		  table[i] = null;
    	  }
    	  this.size = 0;
      }
    
      @SuppressWarnings("unchecked")
	  public TableEntry<K,V>[] toArray(){
    	  int index = 0;
    	  TableEntry<K,V>[] array = (TableEntry<K,V>[]) new TableEntry[this.size];
    	  
    	  for(int i=0;i<table.length;i++) {
    		  if (table[i] == null) continue;
    		  TableEntry<K,V> element = table[i].next;
    		  
    		  while(element != null) {
    			  
    			  array[index] = element;
    			  element = element.next;
    			  index++;
    		  }
    	  }
    	  
    	  return array;
      }
      
      @Override
  	  public Iterator<TableEntry<K, V>> iterator() {
  		
  		  return new IteratorImple();
  	  } 
      
      @SuppressWarnings("unchecked")
	  private void RerangeTable() {
    	  TableEntry<K,V>[] array = (TableEntry<K, V>[]) new TableEntry[this.size];
    	  array = this.toArray();
    	  this.table = (TableEntry<K,V>[]) new TableEntry[2*this.size];
    	  
    	  for(int i=0;i<table.length;i++) {
    		  table[i] = new TableEntry<K,V>();
    	  }
    	  this.size = 0;
    	  
    	  for(int i=0;i<array.length;i++) {
    		  this.add(array[i].key,array[i].value);
    	  }
    	  
    	  this.modificationCount++;
        }
     
      private void tableInitialization() {
    	  for(int i=0;i<table.length;i++) {
    		  table[i] = new TableEntry<K,V>();
    	  }
      }
      
      private int calculateSlot(K key) {

    	  
    	  int hashCode = Math.abs(key.hashCode());
    	  int result =  hashCode % this.table.length;
    	  
    	  return result;
      }
      
      private V add(K key, V value) {
    	 int slot = this.calculateSlot(key);
  	     V oldValue = this.get(key);
  	    
  	    TableEntry<K,V> element = table[slot];
  	    if (oldValue == null) {  // ne postoji u Mapi
  	    	while(element.next != null) {
  	    		element = element.next;
  	    	}
  	    	TableEntry<K,V> newElement = new TableEntry<>(key,value);
  	    	element.next = newElement;
  	    	newElement.next = null;
  	    	oldValue = null;
  	    	this.size++;
  	    	this.modificationCount++;
  	    }else {   // postoji u mapi
  	    	element = element.next;
  	    	while(element != null) {
  	    		if (element.key.equals(key))  element.value = value;
  	    		
  	    		element = element.next;
  	    		break;
  	    	}
  	    }
    	 return oldValue;
      }
      
      
      public static void main(String[] args) {
		   
    	SimpleHashTable<String,Integer> examMarks = new SimpleHashTable<>(2);
    	// fill data:
    	examMarks.put("Ante", 2);
    	examMarks.put("Jasna", 2);
    	examMarks.put("Pero", 5);
    	examMarks.put("Ivana", 5); // overwrites old grade for Ivana
    	/*for(SimpleHashTable.TableEntry<String,Integer> pair : examMarks) {
    		
    	}*/
    	
    	
    	Iterator<SimpleHashTable.TableEntry<String,Integer>> iter = examMarks.iterator();
    	while(iter.hasNext()) {
    	SimpleHashTable.TableEntry<String,Integer> pair = iter.next();
    	System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
    	iter.remove();
    	}
    	System.out.printf("Veličina: %d%n", examMarks.size());
    	
      }
}
