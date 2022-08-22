package resources.annotations;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLGenerator;
import com.gdssecurity.pmd.annotations.HTMLSink;

public class AnnotationsExampleComplexCall3 {

	
	public void example(JspWriter response, HttpServletRequest request) throws IOException {
	    String test = (String) request.getParameter("test");
	    String query = test1(test);
	    response.write(query);
	}
	
	@HTMLGenerator
	@HTMLSink
	public String test1(String test) {
		String query = "";
		String tmp = test2(test);
		query = " query " + tmp;
		return query;
		
	}
	@HTMLGenerator
	@HTMLSink
	public String test2(String test) {
		String query = "";
		query = "query " + test;
		return query;
	}
}
