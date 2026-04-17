package resources.others.bug97;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;

public class Bug97 {

	@SuppressWarnings("unused")
	private PreparedStatement pstmt = null;

	public void search(HttpServletRequest httpRequest, Connection con) throws Exception {
		String badParam  = httpRequest.getParameter("badParam");
		pstmt = con.prepareStatement(badParam);
	}
	
}
