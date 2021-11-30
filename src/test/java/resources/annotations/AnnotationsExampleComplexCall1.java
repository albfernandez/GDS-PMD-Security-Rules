package resources.annotations;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLGenerator;
import com.gdssecurity.pmd.annotations.HTMLSink;

public class AnnotationsExampleComplexCall1 {
	
	public AnnotationsExampleComplexCall1() {
		super();
	}

	@HTMLSink
	@HTMLGenerator
	public static String example1(String param) {
		AnnotationsExampleComplexCall2 example = new AnnotationsExampleComplexCall2();
		return example.complex(param);
	}
	
	public static void exampleCall(JspWriter response, HttpServletRequest request) throws IOException {
		String param = request.getParameter("param");
		response.write(example1(param));
	}
}
