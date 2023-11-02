package hr.fer.zemris.java.hw04.dao;

import java.util.List;

import hr.fer.zemris.java.hw04.models.Poll;
import hr.fer.zemris.java.hw04.models.PollOption;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	
	public List<Poll> getAllPolls();
	
	public List<PollOption> getPollOptionsByPollId(long pollID);
}