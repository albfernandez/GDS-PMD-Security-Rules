package resources.others.bug71;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;

import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class UsingRequestAsParameterFixedExample {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
	    PrintWriter pw = response.getWriter();
	    String html = generateHtml(request);
	    pw.print(html);

	}
	
	@HTMLGenerator
	private String generateHtml(HttpServletRequest request) {
	     String bad = request.getParameter("bad");
	     String html = "<html>" + StringEscapeUtils.escapeHtml4(bad) + "</html>";
	     return html;
	}
}
