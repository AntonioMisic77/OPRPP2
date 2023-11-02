package hr.fer.oprpp2.hw03.models;

public class Band {

	private String Id;
	private String name;
    private String link;
    private String votes;
    
    
    public Band(String Id,String name,String link) {
    	this.Id = Id;
    	this.name = name;
    	this.link = link;
    }


	public String getId() {
		return Id;
	}


	public void setId(String id) {
		Id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}
	
	public String getVotes() {
		return this.votes;
	}
	
	public void setVotes(String votes) {
		this.votes = votes;
	}
    
}
