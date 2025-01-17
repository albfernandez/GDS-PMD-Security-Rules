package resources.others.bug72;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class ExampleSanitizerInSingleton {
	
	
	public static final ExampleSanitizerInSingleton INSTANCE = new ExampleSanitizerInSingleton();
	
	public static final ExampleSanitizerInSingleton getInstance() {
		return INSTANCE;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
	    try (PrintWriter pw = response.getWriter()) {
		    String html = ExampleSanitizerInSingleton.INSTANCE.sanitizeRequest(request);
		    pw.print(html);
		    String html2 = resources.others.bug72.ExampleSanitizerInSingleton.INSTANCE.sanitizeRequest(request);
		    pw.print(html2);
		    
		    pw.print( ExampleSanitizerInSingleton.INSTANCE.sanitizeRequest(request) );
		    pw.print( resources.others.bug72.ExampleSanitizerInSingleton.INSTANCE.sanitizeRequest(request) );
	    }
	}
	
	@HTMLGenerator
	public String sanitizeRequest(HttpServletRequest request) {
		return "";
	}
	
	@HTMLGenerator
	public String sanitizeRequest2(HttpServletRequest request) {
		return "";
	}
	
	

}
