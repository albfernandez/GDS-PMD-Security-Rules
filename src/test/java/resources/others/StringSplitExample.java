package resources.others;

import java.sql.Connection;
import java.sql.SQLException;

public class StringSplitExample {

	private Connection connection;

	
	public void example (String badValue) throws SQLException {
		
		String[] splitted = badValue.split("/");		
		this.connection.prepareStatement("delete from table where id=" + splitted[0]);
		this.connection.prepareStatement("delete from table where id=" + splitted[1]);
	}
	
	

}
