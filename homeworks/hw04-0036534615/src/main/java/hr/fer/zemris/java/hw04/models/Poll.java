package hr.fer.zemris.java.hw04.models;

public class Poll {

	private long id;
	private String title;
	private String message;
	
	
	public Poll(long id,String title,String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}

	
	public long getId() {
		return id;
	}


	public String getTitle() {
		return title;
	}


	public String getMessage() {
		return message;
	}
	
	
}
