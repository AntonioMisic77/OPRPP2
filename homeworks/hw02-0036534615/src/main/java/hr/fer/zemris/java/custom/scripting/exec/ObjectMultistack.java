package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

import hr.fer.oprpp1.custom.collections.EmptyStackException;

public class ObjectMultistack {

	private Map<String,MultiStackEntry> map;
	
	
	public ObjectMultistack() {
		this.map = new HashMap<String,MultiStackEntry>();
	}
	
	public void push(String keyName,ValueWrapper valueWrapper) {
		
		MultiStackEntry newEntry = new MultiStackEntry(valueWrapper, map.get(keyName));	
		this.map.put(keyName, newEntry);
	}
	
	public  ValueWrapper pop(String keyName) {
		
		if(this.isEmpty(keyName)) {
			throw new EmptyStackException();
		}
		
		MultiStackEntry oldEntry = this.map.get(keyName);
		
		if(oldEntry == null) {
			throw new NullPointerException();
		}
		
		this.map.put(keyName, oldEntry.next);
		
		return oldEntry.valueWrapper;
	}
	
	public ValueWrapper peek(String keyName) {
		
		if(this.isEmpty(keyName)) {
			throw new EmptyStackException();
		}
		
		ValueWrapper topStackValue = this.pop(keyName);
		this.push(keyName, topStackValue);
		
		return topStackValue;
	}
	
	
	public boolean isEmpty(String keyName) {
		
		return this.map.get(keyName) == null;
	}
	
	
	private static class MultiStackEntry{
		
		private ValueWrapper valueWrapper;
		private MultiStackEntry next;
		
		
		public MultiStackEntry(ValueWrapper valueWrapper, MultiStackEntry next) {
			this.valueWrapper = valueWrapper;
			this.next = next;
		}
		
	}
	
	public static void main(String[] args) {
		ObjectMultistack multistack = new ObjectMultistack();
		ValueWrapper year = new ValueWrapper(Integer.valueOf(2000));
		multistack.push("year", year);
		ValueWrapper price = new ValueWrapper(200.51);
		multistack.push("price", price);
		System.out.println("Current value for year: "
		+ multistack.peek("year").getValue());
		System.out.println("Current value for price: "
		+ multistack.peek("price").getValue());
		multistack.push("year", new ValueWrapper(Integer.valueOf(1900)));
		System.out.println("Current value for year: "
		+ multistack.peek("year").getValue());
		multistack.peek("year").setValue(
		((Integer)multistack.peek("year").getValue()).intValue() + 50
		);
		System.out.println("Current value for year: "
		+ multistack.peek("year").getValue());
		multistack.pop("year");
		System.out.println("Current value for year: "
		+ multistack.peek("year").getValue());
		multistack.peek("year").add("5");
		System.out.println("Current value for year: "
		+ multistack.peek("year").getValue());
		multistack.peek("year").add(5);
		System.out.println("Current value for year: "
		+ multistack.peek("year").getValue());
		multistack.peek("year").add(5.0);
		System.out.println("Current value for year: "
		+ multistack.peek("year").getValue());

	}
}
