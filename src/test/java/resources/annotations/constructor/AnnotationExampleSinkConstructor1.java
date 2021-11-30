package resources.annotations.constructor;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLSink;

public class AnnotationExampleSinkConstructor1 {
	
	
	private JspWriter response;
	
	@HTMLSink
	public AnnotationExampleSinkConstructor1(String param) throws IOException {
		response.write(param);
	}

}
