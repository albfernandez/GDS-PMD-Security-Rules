package resources.annotations.constructor;

import com.gdssecurity.pmd.annotations.HTMLSink;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationExampleSinkConstructor2 {
	
	private HttpServletRequest request;
	
	@HTMLSink
	public AnnotationExampleSinkConstructor2(HttpServletRequest request, String param) {
		super();
		this.request = request;
	}
	
	public void caller1() {
		new AnnotationExampleSinkConstructor2(request, null);
	}
	
	public void caller2() {
		new AnnotationExampleSinkConstructor2(null, "param");
	}
	
	public void caller3() {
		String badParam = request.getParameter("bad");
		new AnnotationExampleSinkConstructor2(request, badParam);
	}

}
