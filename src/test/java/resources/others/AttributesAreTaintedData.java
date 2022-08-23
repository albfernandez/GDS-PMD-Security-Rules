package resources.others;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.text.StringEscapeUtils;

public class AttributesAreTaintedData { 

	private String badData = null;
	
	public void badMethodSql(Connection con) throws Exception {		
		String query = "select * From " + badData;
		boolean test  = con.prepareStatement(query) != null;		
	}
	
	public void badMethodSql2(Connection con) throws Exception {		
		String query = "select * From " + badData;
		PreparedStatement pstmt  = con.prepareStatement(query, 0);		
	}
	
	public void badMethodWithTry(JspWriter writer) {
		String html = "abc " + badData;
		try {
			if (html != null) {
				writer.write(html);				
			}
		}
		catch (Exception e) {
			//
		}
	}

	public void badMethod(JspWriter writer)  throws Exception{
		String html = "abc " + badData;
		writer.write(html);
		writer.write(this.badData);
	}
	
	public void goodMethod(JspWriter writer) throws IOException {
		writer.write(StringEscapeUtils.escapeHtml4(badData));
		writer.write(StringEscapeUtils.escapeHtml4(this.badData));
	}



	
	
}
