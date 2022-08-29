package resources.others;

import java.sql.Connection;

public class AttributesAreTaintedData2 { 

	private Connection con = null;
	private String badData = null;
	
	public void badMethodSql() throws Exception {		
		String query = "select * From " + badData;
		con.prepareStatement(query);		
	}
	 public void dropTable() throws Exception {
		 String query = "Drop Table "+badData;

		 try {
			 con.prepareStatement(query);
		 } catch (Exception e) {
		 }		 
		 return;
	 }
	 
	 public void badMethodSql2() throws Exception {		
			String query = "select * From " + badData;
			boolean result = con.prepareStatement(query).executeUpdate() > 0;		
	}
}
