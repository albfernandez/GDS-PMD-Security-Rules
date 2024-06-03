package resources.others;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class UseOfAttributes {

	Connection c;
	String sql;
	Statement stmt;
	
	public List<Object> test (HttpServletRequest httpRequest) throws Exception {
		String param_req = null;
		List<Object> list = new ArrayList<Object>();
		try {
			param_req = httpRequest.getParameter("param");
			sql = "select * from dual" + 
			" where 1 = 1 " +
			" and active = 1 ";
			if (param_req != null) {
				sql += " and column = '" + param_req + "'";
			}
			
			
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				list.add(res.getString(1));
			}
		}
		finally {
			
		}
		
		return list;
		
		
	}
}
