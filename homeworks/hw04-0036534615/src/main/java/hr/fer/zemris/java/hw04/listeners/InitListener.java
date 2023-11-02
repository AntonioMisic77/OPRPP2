package hr.fer.zemris.java.hw04.listeners;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

@WebListener
public class InitListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		
		ServletContext context = sce.getServletContext();
		String propertiesFilePath = context.getRealPath("/WEB-INF/dbsettings.properties");
		
		String connectionString = "";
		
		try {
			connectionString= getConnectionString(propertiesFilePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		
		try {
			cpds.setDriverClass("org.apache.derby.client.ClientAutoloadedDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		
		cpds.setJdbcUrl(connectionString);
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
		
		try {
			Connection connection = cpds.getConnection();
			ResultSet polls = connection.getMetaData().getTables(null, "IVICA", "Polls", null);
			ResultSet pollOptions = connection.getMetaData().getTables(null,"IVICA","PollOptions",null);
			
			/*if(!polls.next()) {
				createPollsTable(connection);
			}
			
			if(!pollOptions.next()) {
				createPollsOptionTable(connection);
			}*/
			
			
			if(dbIsEmpty(connection)) {
				seedDb(connection,context);
			}
			
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void contextDestroyed(ServletContextEvent sce) {
		
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getConnectionString(String filePath) throws FileNotFoundException, IOException {
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(filePath));
		
		String host = properties.getProperty("host");
		checkProperty(host, "host");
		
		String port = properties.getProperty("port");
		checkProperty(port, "port");
		
		String name = properties.getProperty("name");
		checkProperty(name, "name");
		
		String user = properties.getProperty("user");
		checkProperty(user, "user");
		
		String password = properties.getProperty("password");
		checkProperty(password, "password");
		
		return "jdbc:derby://"+host+":"+port+"/"+name+";user="+user+";password="+password;
	}
	
	private void checkProperty(String value,String name) {
		if(value.equals(null)) {
			throw new RuntimeException("Property"+name+"is null.");
		}
	}

	private void createPollsTable(Connection connection) {
		
		String query = "CREATE TABLE Polls("
					   +"  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
					   +"  title VARCHAR(150) NOT NULL,"
					   +"  message CLOB(2048) NOT NULL"
					   + ")";
		try {
			PreparedStatement pst = connection.prepareStatement(query);
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createPollsOptionTable(Connection connection)  {
		String query = "CREATE TABLE PollOptions("
					   +" id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
					   +" optionTitle VARCHAR(100) NOT NULL,"
					   +" optionLink  VARCHAR(150) NOT NULL,"
					   +" pollId BIGINT,"
					   +" votesCount BIGINT,"
					   +" FOREIGN KEY (pollID) REFERENCES Polls(id)"
					   +")";
		
		try {
			PreparedStatement pst = connection.prepareStatement(query);
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean dbIsEmpty(Connection connection) {
		String query = "SELECT Count(*) from polls";
		
		try {
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			rs.next();
		
			return rs.getInt(1) == 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private void seedDb(Connection connection,ServletContext context) {
		String pollsSql = "INSERT INTO Polls (title,message) VALUES (?,?)";
		String pollsOptionSql = "INSERT INTO PollOptions (optionTitle,optionLink,pollID,votesCount) VALUES (?,?,?,?)";
		
		try {
			PreparedStatement pstPolls = connection.prepareStatement(pollsSql,Statement.RETURN_GENERATED_KEYS);
			PreparedStatement pstPollsOptions = connection.prepareStatement(pollsOptionSql);
			
			List<String> pollsFile = Files.readAllLines(
											Paths.get(context.getRealPath("/WEB-INF/polls.txt")),
											StandardCharsets.UTF_8
											);
			Random random = new Random();
			
			for(String poll : pollsFile) {
				String[] pollElements = poll.split("\\t+");
				String pollTitle = pollElements[1];
				String pollMessage = pollElements[2];
				
				pstPolls.setString(1, pollTitle);
				pstPolls.setString(2, pollMessage);
				pstPolls.executeUpdate();
				
				ResultSet rs = pstPolls.getGeneratedKeys();
				
				if(rs.next()) {
					long pollID = rs.getLong(1);
					
					List<String> pollOptionsFile = Files.readAllLines(
								Paths.get(context.getRealPath("/WEB-INF/"+pollElements[0])),
								StandardCharsets.UTF_8
							);
					
					for(String pollOption : pollOptionsFile) {
						String[] pollOptionElements = pollOption.split("\\t+");
						pstPollsOptions.setString(1, pollOptionElements[0]);
						pstPollsOptions.setString(2, pollOptionElements[1]);
						pstPollsOptions.setLong(3,pollID);
						pstPollsOptions.setLong(4, random.nextInt(50));
						
						pstPollsOptions.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}