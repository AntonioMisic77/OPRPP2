package hr.fer.zemris.java.hw04.models;

public class PollOption {

	private long id;
	private String optionTitle;
	private String optionLink;
	private long pollID;
	private long votesCount;
	
	public PollOption(long id,String optionTitle,String optionLink,long pollID,long votesCount) {
		this.id = id;
		this.optionLink = optionLink;
		this.optionTitle = optionTitle;
		this.pollID = pollID;
		this.votesCount = votesCount;
	}

	public long getId() {
		return id;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public String getOptionLink() {
		return optionLink;
	}

	public long getPollID() {
		return pollID;
	}

	public long getVotesCount() {
		return votesCount;
	}
	
}
