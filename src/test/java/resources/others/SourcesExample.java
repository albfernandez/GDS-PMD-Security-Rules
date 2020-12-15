package resources.others;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

public class SourcesExample {

	public void testServletRequest(HttpServletRequest request, JspWriter out) throws IOException {
		out.write(request.getParameter("bad"));
		out.write(request.getParameterNames().nextElement());
		out.write(request.getParameterMap().get("bad")[0]);
		out.write(request.getParameterValues("bad")[0]);
	}
	
	public void testHttpServletRequest(ServletRequest request, JspWriter out) throws IOException {
		out.write(request.getParameter("bad"));
		out.write(request.getParameterNames().nextElement());
		out.write(request.getParameterMap().get("bad")[0]);
		out.write(request.getParameterValues("bad")[0]);
	}
}
