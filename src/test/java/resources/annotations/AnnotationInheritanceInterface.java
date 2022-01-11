package resources.annotations;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLSink;

public interface AnnotationInheritanceInterface {

	
	@HTMLSink	
	public void example1(JspWriter response, String param) throws IOException;
	
	@HTMLSink
	public void exampleCall(JspWriter response, String param) throws IOException;
	
}
