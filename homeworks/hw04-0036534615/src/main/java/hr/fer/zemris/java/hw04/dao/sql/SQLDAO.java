package hr.fer.zemris.java.hw04.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.dao.DAO;
import hr.fer.zemris.java.hw04.models.Poll;
import hr.fer.zemris.java.hw04.models.PollOption;


public class SQLDAO implements DAO {

	
	
	public List<Poll> getAllPolls() {
		Connection connection = SQLConnectionProvider.getConnection();
		
		String query = "SELECT * FROM Polls";
		
		List<Poll> polls = new ArrayList<Poll>();
		
		try {
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				long id = rs.getLong("id");
				String title = rs.getString("title");
				String message = rs.getString("message");
				
				polls.add(new Poll(id,title,message));
			}
			
			pst.close();
			rs.close();
			
			return polls;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<PollOption> getPollOptionsByPollId(long pollID) {
		Connection connection = SQLConnectionProvider.getConnection();
		String query = "SELECT * FROM PoolOptions WHERE pollID = ?";
		
		List<PollOption> pollOptions = new ArrayList<PollOption>();
		
		try {
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setLong(1, pollID);
			
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				long id = rs.getLong(1);
				String optionTitle = rs.getString(2);
				String optionLink = rs.getString(3);
				long votesCount = rs.getLong(5);
				
				pollOptions.add(new PollOption(id,optionTitle,optionLink,pollID,votesCount));
				
			}
			return pollOptions;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

}