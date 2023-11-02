package hr.fer.oprpp2.hw03.models;

public class PoolResult {

	private String id;
	private String votes;
	
	public PoolResult(String id,String votes) {
		this.id = id;
		this.votes = votes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVotes() {
		return votes;
	}

	public void setVotes(String votes) {
		this.votes = votes;
	}
	
}
