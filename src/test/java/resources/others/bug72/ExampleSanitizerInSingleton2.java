package resources.others.bug72;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

public class ExampleSanitizerInSingleton2 {

	  public void service(HttpServletRequest request, JspWriter out) throws Exception {
		  
	      String html2 = ExampleSanitizerInSingleton.getInstance().sanitizeRequest(request);
	      out.print(html2);
	      
	      
		  String html = ExampleSanitizerInSingleton.INSTANCE.sanitizeRequest(request);
	      out.print(html);
	      

	  }
}
