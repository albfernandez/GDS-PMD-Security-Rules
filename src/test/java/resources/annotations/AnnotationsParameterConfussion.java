package resources.annotations;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLSink;

public class AnnotationsParameterConfussion {
	
	public void simpleCall(String param) {
		//nop
		// 2.27 and before take this "param" as tainted for this method and others
		
	}
	
	@HTMLSink
	public void sink(JspWriter response, String param) throws IOException  {
		response.write(param); // no warning shoud be triggered here
	}
}
