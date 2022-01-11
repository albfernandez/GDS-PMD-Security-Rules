package resources.annotations;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLSink;

public class AnnotationInheritanceClass implements AnnotationInheritanceInterface {

	@HTMLSink	
	@Override
	public void example1(JspWriter response, String param) throws IOException {
		exampleCall(response, param);
	}
	
	@HTMLSink
	@Override
	public void exampleCall(JspWriter response, String param) throws IOException  {
		response.write(param);
	}
}
